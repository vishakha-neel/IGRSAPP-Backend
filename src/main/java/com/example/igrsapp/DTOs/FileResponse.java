package com.example.igrsapp.DTOs;

public class FileResponse {

    private String imageBase64;
    private int totalPages;

    public FileResponse(String imageBase64, int totalPages) {
        this.imageBase64 = imageBase64;
        this.totalPages = totalPages;
    }

    // Getters and setters
    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }    
}
