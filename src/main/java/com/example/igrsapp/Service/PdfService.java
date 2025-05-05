package com.example.igrsapp.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.igrsapp.Repository.mainigrs.BulkPageVerifyRepository;
import com.example.igrsapp.Repository.mainigrs.DataFetchRepository;
import com.example.igrsapp.modals.mainigrs.BulkPageVerify;
import com.example.igrsapp.modals.mainigrs.BulkPageVerify.ActionType;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

@Service
public class PdfService {
	
	@Autowired
	DataFetchRepository repository;
	
	@Autowired
	BulkPageVerifyRepository verifyRepository;
	
	@Value("${masterPdfLocation}")
	String masterPdfLocation;

	@Value("${modifiedPdfLocation}")
	String modifiedPdfLocation;

	@Value("${backupPdfLocation}")
	String backupPdfLocation;
	
	public void replacePageFromPdf(String fileID, String district, String subDistrict, String pageNumber,
			MultipartFile file) throws IOException {

		district = district.contains("(") ? district.substring(0, district.lastIndexOf("(")) : district;
		subDistrict = subDistrict.contains("(") ? subDistrict.substring(0, subDistrict.lastIndexOf("(")) : subDistrict;

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		String baseLocation = repository.getLocation(fileID, district, subDistrict);
		String originalPdfPath = baseLocation+File.separator+fileID+ ".pdf";


		String masterPdfPath = saveMasterPdfOnce(file, timeStamp);

		BulkPageVerify bulkPageVerify = new BulkPageVerify();
		bulkPageVerify.setAction(ActionType.REPLACE);
		bulkPageVerify.setDistrict(district);
		bulkPageVerify.setSubdistrict(subDistrict);
		bulkPageVerify.setFileId(fileID);
		bulkPageVerify.setPdfPages(pageNumber);
		bulkPageVerify.setMasterPages("1;"); // Only one page being replaced
		bulkPageVerify.setOriginalPdfLocation(originalPdfPath);
		bulkPageVerify.setMasterPdfLocation(masterPdfPath);
		bulkPageVerify.setStatus("P");
		bulkPageVerify.setReplaceDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		bulkPageVerify.setReplacerName(SecurityContextHolder.getContext().getAuthentication().getName());
		bulkPageVerify.setVerifiedBy("-");

		File originalFile = new File(originalPdfPath);
		if (!originalFile.exists()) {
			throw new FileNotFoundException("Original PDF not found: " + originalPdfPath);
		}

		File backupFile = new File(backupPdfLocation + File.separator + timeStamp + fileID + ".pdf");
		Files.copy(originalFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		bulkPageVerify.setBackUpLocation(backupFile.getAbsolutePath());

		File modifiedFile = new File(modifiedPdfLocation + File.separator + timeStamp + fileID + ".pdf");

		try (PdfReader reader = new PdfReader(originalFile);
				PdfReader masterReader = new PdfReader(masterPdfPath);
				PdfWriter writer = new PdfWriter(modifiedFile);
				PdfDocument pdfDoc = new PdfDocument(reader, writer);
				PdfDocument masterDoc = new PdfDocument(masterReader)) {
			int targetPageIndex = Integer.parseInt(pageNumber.trim());

			if (targetPageIndex > 0 && targetPageIndex <= pdfDoc.getNumberOfPages()) {
				PdfPage replacementPage = masterDoc.getPage(1).copyTo(pdfDoc);
				pdfDoc.removePage(targetPageIndex);
				pdfDoc.addPage(targetPageIndex, replacementPage);
			} else {
				throw new IllegalArgumentException("Invalid page number: " + targetPageIndex);
			}
		}

		bulkPageVerify.setModifiedPdfLocation(modifiedFile.getAbsolutePath());
		verifyRepository.save(bulkPageVerify);
	}	
	
	
	
    public byte[] getPageImage(String fileId, String district, String subDistrict, int pageNumber) throws IOException 
    {
    	
    	String baseLocation=repository.getLocation(fileId, district, subDistrict);
        String filePath = baseLocation + fileId + ".pdf";

        File pdfFile = new File(filePath);
        if (!pdfFile.exists()) 
        {
            throw new IOException("PDF file not found at: " + filePath);
        }

        try (PDDocument document = PDDocument.load(pdfFile)) 
        {
            if (pageNumber <= 0 || pageNumber > document.getNumberOfPages()) {
                throw new IllegalArgumentException("Invalid page number: " + pageNumber);
            }

            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage image = pdfRenderer.renderImageWithDPI(pageNumber - 1, 10, ImageType.RGB);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);

            return baos.toByteArray();
        }
    }
    
    private String saveMasterPdfOnce(MultipartFile imageFile, String timestamp) throws IOException {
        String pdfPath = masterPdfLocation + File.separator + timestamp + ".pdf";

        try (PdfWriter writer = new PdfWriter(pdfPath);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc, PageSize.A4)) {

            // Remove margins if you want full-page image
            document.setMargins(0, 0, 0, 0);

            // Create ImageData from the uploaded MultipartFile
            ImageData imageData = ImageDataFactory.create(imageFile.getBytes());

            // Create Image object
            Image image = new Image(imageData);

            // Scale image to fit A4
            image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());

            // Center the image on the page
            float x = (PageSize.A4.getWidth() - image.getImageScaledWidth()) / 2;
            float y = (PageSize.A4.getHeight() - image.getImageScaledHeight()) / 2;
            image.setFixedPosition(x, y);

            // Add image to the document
            document.add(image);

        } catch (Exception e) {
            throw new IOException("Error creating PDF from image: " + e.getMessage(), e);
        }

        return pdfPath;
    }
    
    public void deletePageFromPdf(String fileID, String district, String subDistrict, String pageNumber) throws IOException {
        district = district.contains("(") ? district.substring(0, district.lastIndexOf("(")) : district;
        subDistrict = subDistrict.contains("(") ? subDistrict.substring(0, subDistrict.lastIndexOf("(")) : subDistrict;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String baseLocation = repository.getLocation(fileID, district, subDistrict);
        String originalPdfPath = baseLocation + File.separator + fileID + ".pdf";

        BulkPageVerify bulkPageVerify = new BulkPageVerify();
        bulkPageVerify.setAction(ActionType.DELETE);
        bulkPageVerify.setDistrict(district);
        bulkPageVerify.setSubdistrict(subDistrict);
        bulkPageVerify.setFileId(fileID);
        bulkPageVerify.setPdfPages(pageNumber);
        bulkPageVerify.setMasterPages("-");
        bulkPageVerify.setOriginalPdfLocation(originalPdfPath);
        bulkPageVerify.setMasterPdfLocation("-");
        bulkPageVerify.setStatus("P");
        bulkPageVerify.setReplaceDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        bulkPageVerify.setReplacerName(SecurityContextHolder.getContext().getAuthentication().getName());
        bulkPageVerify.setVerifiedBy("-");

        File originalFile = new File(originalPdfPath);
        if (!originalFile.exists()) {
            throw new FileNotFoundException("Original PDF not found: " + originalPdfPath);
        }

        // Backup original PDF
        File backupFile = new File(backupPdfLocation + File.separator + timeStamp + fileID + ".pdf");
        Files.copy(originalFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        bulkPageVerify.setBackUpLocation(backupFile.getAbsolutePath());

        // Create modified file path
        File modifiedFile = new File(modifiedPdfLocation + File.separator + timeStamp + fileID + ".pdf");

        // Delete the page
        try (PdfReader reader = new PdfReader(originalFile.getAbsolutePath());
             PdfWriter writer = new PdfWriter(modifiedFile.getAbsolutePath());
             PdfDocument originalPdf = new PdfDocument(reader);
             PdfDocument modifiedPdf = new PdfDocument(writer)) {

            int pageToDelete;
            try {
                pageToDelete = Integer.parseInt(pageNumber);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid page number format: " + pageNumber);
            }

            if (pageToDelete <= 0 || pageToDelete > originalPdf.getNumberOfPages()) {
                throw new IllegalArgumentException("Page number out of range: " + pageToDelete);
            }

            // Copy all pages except the one to delete
            for (int i = 1; i <= originalPdf.getNumberOfPages(); i++) {
                if (i != pageToDelete) {
                    originalPdf.copyPagesTo(i, i, modifiedPdf);
                }
            }
        }

        // Save the bulk verify request for approval or auditing
        bulkPageVerify.setModifiedPdfLocation(modifiedFile.getAbsolutePath());
        verifyRepository.save(bulkPageVerify);
    }
    
    
    public void addPageToPdf(String fileID, String district, String subDistrict, String pageNumber, MultipartFile file) throws IOException {
        district = district.contains("(") ? district.substring(0, district.lastIndexOf("(")) : district;
        subDistrict = subDistrict.contains("(") ? subDistrict.substring(0, subDistrict.lastIndexOf("(")) : subDistrict;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String baseLocation = repository.getLocation(fileID, district, subDistrict);
        String originalPdfPath = baseLocation + File.separator + fileID + ".pdf";

        // Save master PDF and get path
        String masterPdfPath = saveMasterPdfOnce(file, timeStamp);

        BulkPageVerify bulkPageVerify = new BulkPageVerify();
        bulkPageVerify.setAction(ActionType.ADD);
        bulkPageVerify.setDistrict(district);
        bulkPageVerify.setSubdistrict(subDistrict);
        bulkPageVerify.setFileId(fileID);
        bulkPageVerify.setPdfPages(pageNumber);
        bulkPageVerify.setMasterPages("1;");
        bulkPageVerify.setOriginalPdfLocation(originalPdfPath);
        bulkPageVerify.setMasterPdfLocation(masterPdfPath);
        bulkPageVerify.setStatus("P");
        bulkPageVerify.setReplaceDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        bulkPageVerify.setReplacerName(SecurityContextHolder.getContext().getAuthentication().getName());
        bulkPageVerify.setVerifiedBy("-");

        File originalFile = new File(originalPdfPath);
        if (!originalFile.exists()) {
            throw new FileNotFoundException("Original PDF not found: " + originalPdfPath);
        }

        // Backup original PDF
        File backupFile = new File(backupPdfLocation + File.separator + timeStamp + fileID + ".pdf");
        Files.copy(originalFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        bulkPageVerify.setBackUpLocation(backupFile.getAbsolutePath());

        // Prepare modified output file
        File modifiedFile = new File(modifiedPdfLocation + File.separator + timeStamp + fileID + ".pdf");

        try (
            PdfReader targetReader = new PdfReader(originalFile);
            PdfWriter targetWriter = new PdfWriter(modifiedFile);
            PdfDocument targetDoc = new PdfDocument(targetReader, targetWriter);
            PdfReader masterReader = new PdfReader(masterPdfPath);
            PdfDocument masterDoc = new PdfDocument(masterReader);
        ) {
            int targetPageNumber = Integer.parseInt(pageNumber);  // Insert after this page (0-based logic)
            int masterPageNumber = 1; // Always insert the first page of the master PDF

            if (targetPageNumber >= 0 && targetPageNumber <= targetDoc.getNumberOfPages() &&
                masterPageNumber >= 1 && masterPageNumber <= masterDoc.getNumberOfPages()) {

                PdfPage masterPage = masterDoc.getPage(masterPageNumber);
                targetDoc.addPage(targetPageNumber, masterPage.copyTo(targetDoc)); // insert after specified page
            } else {
                throw new IllegalArgumentException("Invalid page number: cannot insert.");
            }

            bulkPageVerify.setModifiedPdfLocation(modifiedFile.getAbsolutePath());
            verifyRepository.save(bulkPageVerify);  

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error while processing the PDF files", e);
        }
    }
	
}