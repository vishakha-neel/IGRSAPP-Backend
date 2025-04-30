package com.example.igrsapp.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfService {
    private final String uploadDir = "uploads/";
    private final String imageDir = "images/";

    public String uploadPdf(MultipartFile file) throws IOException {
        // Ensure directories exist
        Files.createDirectories(Paths.get(uploadDir));
        Files.createDirectories(Paths.get(imageDir));

        // Save PDF file
        String fileId = UUID.randomUUID().toString();
        String fileName = fileId + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, file.getBytes());

        // Load PDF and get page count
        File pdfFile = filePath.toFile();
        PDDocument document = PDDocument.load(pdfFile);
        int pageCount = document.getNumberOfPages();

        // Split PDF into images
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        for (int page = 0; page < pageCount; page++) {
            BufferedImage image = pdfRenderer.renderImageWithDPI(page, 30); // 300 DPI
            File outputFile = new File(imageDir + fileId + "_page_" + (page + 1) + ".png");
            ImageIO.write(image, "PNG", outputFile);
        }

        document.close();
        return fileId + "," + pageCount; // Return fileId and pageCount as a string
    }

	/*
	 * public byte[] getPageImage(String fileId, String district , String
	 * subDistrict , int pageNumber) throws IOException { // Check if PDF exists and
	 * get page count File pdfFile = findPdfFile(fileId); if (pdfFile == null) {
	 * throw new RuntimeException("PDF not found"); } PDDocument document =
	 * PDDocument.load(pdfFile); int pageCount = document.getNumberOfPages();
	 * document.close();
	 * 
	 * if (pageNumber < 1 || pageNumber > pageCount) { throw new
	 * RuntimeException("Invalid page number"); }
	 * 
	 * Path imagePath = Paths.get("D:/PdfForBlankPage/" + "90012-2_0_17" + ".png");
	 * if (!Files.exists(imagePath)) { throw new
	 * RuntimeException("Page image not found"); } return
	 * Files.readAllBytes(imagePath); }
	 */
    
    public byte[] getPageImage(String fileId, String district, String subDistrict, int pageNumber) throws IOException {
        // Construct the file path
        // Example path: /path/to/pdfs/{district}/{subDistrict}/{fileId}.pdf
    	
        String basePath = "D:/PdfForBlankPage/"; // <-- CHANGE THIS to your actual folder
        String filePath = basePath + "1-5-2000-Pune-0001" + ".pdf";

        File pdfFile = new File(filePath);
        if (!pdfFile.exists()) {
            throw new IOException("PDF file not found at: " + filePath);
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            if (pageNumber <= 0 || pageNumber > document.getNumberOfPages()) {
                throw new IllegalArgumentException("Invalid page number: " + pageNumber);
            }

            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage image = pdfRenderer.renderImageWithDPI(pageNumber - 1, 10, ImageType.RGB);
            // (pageNumber - 1) because PDFBox is 0-indexed

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);

            return baos.toByteArray();
        }
    }

    private File findPdfFile(String fileId) {
        File uploadDirFile = new File(uploadDir);
        File[] files = uploadDirFile.listFiles((dir, name) -> name.startsWith(fileId));
        return files != null && files.length > 0 ? files[0] : null;
    }
}
