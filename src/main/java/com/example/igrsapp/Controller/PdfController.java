package com.example.igrsapp.Controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.igrsapp.Repository.mainigrs.BulkPageVerifyRepository;
import com.example.igrsapp.Service.PdfService;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {
    
    @Autowired
    private PdfService pdfService;
    
    @Autowired
    private BulkPageVerifyRepository bulkPageRepository;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Backend is healthy");
    }

    @PostMapping("/{fileID}/{district}/{subDistrict}/{pageNumber}")
    public ResponseEntity<String> uploadPage(
            @PathVariable String fileID,
            @PathVariable String district,
            @PathVariable String subDistrict,
            @PathVariable String pageNumber,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if(bulkPageRepository.findByFileId(fileID)!=null)
            {
            	return ResponseEntity.ok("File already in the Pending status");
            }
            pdfService.replacePageFromPdf(fileID, district, subDistrict, pageNumber, file);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading file");
        }
    }
    
    @PostMapping("add/{fileID}/{district}/{subDistrict}/{pageNumber}")
    public ResponseEntity<String> addPage(
            @PathVariable String fileID,
            @PathVariable String district,
            @PathVariable String subDistrict,
            @PathVariable String pageNumber,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if(bulkPageRepository.findByFileId(fileID)!=null)
            {
            	return ResponseEntity.ok("File already in the Pending status");
            }
            pdfService.addPageToPdf(fileID, district, subDistrict, pageNumber, file);
            return ResponseEntity.ok("Page Added successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading file");
        }
    }
    
    @PostMapping("delete/{fileID}/{district}/{subDistrict}/{pageNumber}")
    public ResponseEntity<String> deletePage(
            @PathVariable String fileID,
            @PathVariable String district,
            @PathVariable String subDistrict,
            @PathVariable String pageNumber
    ) {
        try {
            if(bulkPageRepository.findByFileId(fileID)!=null)
            {
            	return ResponseEntity.ok("File already in the Pending status");
            }
            pdfService.deletePageFromPdf(fileID, district, subDistrict, pageNumber);
            return ResponseEntity.ok("Page Deleted successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error while deleting the page");
        }
    }

    @GetMapping("/{fileId}/{district}/{subDistrict}/{pageNumber}")
    public ResponseEntity<String> getPageImageBase64(@PathVariable String fileId , @PathVariable String district , @PathVariable String subDistrict , @PathVariable int pageNumber) throws IOException 
    {
    	byte[] imageBytes = pdfService.getPageImage(fileId, district , subDistrict ,pageNumber);
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        return ResponseEntity.ok(base64);
    }

    @GetMapping("/test")
    public void getTest() throws IOException {
        System.out.println(" In Test Api ........");
    }
}
