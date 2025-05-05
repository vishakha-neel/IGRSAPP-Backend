package com.example.igrsapp.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.igrsapp.DTOs.FileRequest;
import com.example.igrsapp.DTOs.FileResponse;
import com.example.igrsapp.Service.DataEntryService;

@RestController
public class DataEntryController {

    @Autowired
    private DataEntryService dataEntryService;

    @PostMapping("/getImageByFileId")
    public ResponseEntity<FileResponse> getImage(@RequestBody FileRequest request) {
        FileResponse response = dataEntryService.getPdfFirstPageAsImage(request);
        return ResponseEntity.ok(response);
    }
    
}
