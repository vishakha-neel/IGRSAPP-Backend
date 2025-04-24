package com.example.igrsapp.Controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.igrsapp.Service.PdfService;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {
    
    @Autowired
    private PdfService pdfService;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Backend is healthy");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("upload --------- ");
        String result = pdfService.uploadPdf(file);
        return ResponseEntity.ok(result);
    }

    // @GetMapping("/{fileId}/page/{pageNumber}")
    // public ResponseEntity<byte[]> getPageImage(@PathVariable String fileId, @PathVariable int pageNumber) throws IOException {
    //     byte[] imageBytes = pdfService.getPageImage(fileId, pageNumber);
    //     return ResponseEntity.ok()
    //             .contentType(MediaType.IMAGE_PNG)
    //             .body(imageBytes);
    // }

    @GetMapping("/{fileId}/page/{pageNumber}")
    public ResponseEntity<String> getPageImageBase64(@PathVariable String fileId, @PathVariable int pageNumber) throws IOException {
        byte[] imageBytes = pdfService.getPageImage(fileId, pageNumber);
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        return ResponseEntity.ok(base64);
    }

    @GetMapping("/test")
    public void getTest() throws IOException {
        System.out.println(" In Test Api ........");
    }
}
