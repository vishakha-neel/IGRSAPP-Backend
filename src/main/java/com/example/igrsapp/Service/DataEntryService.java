package com.example.igrsapp.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import com.example.igrsapp.DTOs.FileRequest;
import com.example.igrsapp.DTOs.FileResponse;
import com.example.igrsapp.Repository.dataEntry.DataEntryRepository;
import com.example.igrsapp.modals.dataEntry.DataEntry;

import java.awt.image.BufferedImage;


@Service
public class DataEntryService {

    private final DataEntryRepository dataEntryRepository;

    public DataEntryService(DataEntryRepository dataEntryRepository) {
        this.dataEntryRepository = dataEntryRepository;
    }

    public FileResponse getPdfFirstPageAsImage(FileRequest request) {
        // Example logic to fetch PDF file path from DB
        DataEntry entry = dataEntryRepository.findLocationByFileIdAndDistrictsAndSubDistrict(request.getFileId(),request.getDistrict(), request.getSubdistrict());
        System.out.println(entry.getLocation()+request.getFileId()+".pdf");
        File pdfFile = new File(entry.getLocation()+request.getFileId()+".pdf");

        // Convert first page to image and get total pages
        String imageBase64 = convertPdfFirstPageToBase64(pdfFile);
        int totalPages = getTotalPdfPages(pdfFile);

        return new FileResponse(imageBase64, totalPages);
    }

    private String convertPdfFirstPageToBase64(File pdfFile) {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, 150);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert PDF to image", e);
        }
    }

    private int getTotalPdfPages(File pdfFile) {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            return document.getNumberOfPages();
        } catch (IOException e) {
            throw new RuntimeException("Failed to count PDF pages", e);
        }
    }
    
}
