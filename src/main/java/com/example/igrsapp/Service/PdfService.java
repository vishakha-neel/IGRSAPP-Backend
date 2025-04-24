package com.example.igrsapp.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
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
            BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300); // 300 DPI
            File outputFile = new File(imageDir + fileId + "_page_" + (page + 1) + ".png");
            ImageIO.write(image, "PNG", outputFile);
        }

        document.close();
        return fileId + "," + pageCount; // Return fileId and pageCount as a string
    }

    public byte[] getPageImage(String fileId, int pageNumber) throws IOException {
        // Check if PDF exists and get page count
        File pdfFile = findPdfFile(fileId);
        if (pdfFile == null) {
            throw new RuntimeException("PDF not found");
        }
        PDDocument document = PDDocument.load(pdfFile);
        int pageCount = document.getNumberOfPages();
        document.close();

        if (pageNumber < 1 || pageNumber > pageCount) {
            throw new RuntimeException("Invalid page number");
        }

        Path imagePath = Paths.get(imageDir + fileId + "_page_" + pageNumber + ".png");
        if (!Files.exists(imagePath)) {
            throw new RuntimeException("Page image not found");
        }
        return Files.readAllBytes(imagePath);
    }

    private File findPdfFile(String fileId) {
        File uploadDirFile = new File(uploadDir);
        File[] files = uploadDirFile.listFiles((dir, name) -> name.startsWith(fileId));
        return files != null && files.length > 0 ? files[0] : null;
    }
}
