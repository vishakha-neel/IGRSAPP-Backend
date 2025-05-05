package com.example.igrsapp.modals.mainigrs;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BulkPageVerify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ActionType action;
    private String district;
    private String subdistrict;
    private String replaceDate;
    private String fileId;

    private String masterPages;
    private String pdfPages;

    private String masterPdfLocation;
    private String originalPdfLocation;
    private String modifiedPdfLocation;
    private String backUpLocation;

    private String status;
    private String replacerName;
    private String verifiedBy;

     // Enum for action types
    public enum ActionType {
        REPLACE,
        ADD,
        DELETE
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ActionType getAction() { return action; }
    public void setAction(ActionType action) { this.action = action; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getSubdistrict() { return subdistrict; }
    public void setSubdistrict(String subdistrict) { this.subdistrict = subdistrict; }

    public String getReplaceDate() { return replaceDate; }
    public void setReplaceDate(String replaceDate) { this.replaceDate = replaceDate; }

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }

    public String getMasterPages() { return masterPages; }
    public void setMasterPages(String masterPages) { this.masterPages = masterPages; }

    public String getPdfPages() { return pdfPages; }
    public void setPdfPages(String pdfPages) { this.pdfPages = pdfPages; }

    public String getBackUpLocation() { return backUpLocation; }
    public void setBackUpLocation(String backUpLocation) { this.backUpLocation = backUpLocation; }
    
    public String getMasterPdfLocation() { return masterPdfLocation; }
    public void setMasterPdfLocation(String masterPdfLocation) { this.masterPdfLocation = masterPdfLocation; }

    public String getOriginalPdfLocation() { return originalPdfLocation; }
    public void setOriginalPdfLocation(String originalPdfLocation) { this.originalPdfLocation = originalPdfLocation; }

    public String getModifiedPdfLocation() { return modifiedPdfLocation; }
    public void setModifiedPdfLocation(String modifiedPdfLocation) { this.modifiedPdfLocation = modifiedPdfLocation; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReplacerName() { return replacerName; }
    public void setReplacerName(String replacerName) { this.replacerName = replacerName; }

    public String getVerifiedBy() { return verifiedBy; }
    public void setVerifiedBy(String verifiedBy) { this.verifiedBy = verifiedBy; }

}