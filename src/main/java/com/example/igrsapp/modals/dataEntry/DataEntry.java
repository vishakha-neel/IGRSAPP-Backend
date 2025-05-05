package com.example.igrsapp.modals.dataEntry;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DataEntry")
public class DataEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Sno")
	private long sno;

	@Column(name = "UserId")
	private String userId;

	@Column(name = "FileId")
	private String fileId;
	
	@Column(name = "Location")
	private String location;
	
	@Column(name = "Districts")
	private String districts;
	
	@Column(name = "subDistrict")
	private String subDistrict;
	
	@Column(name = "AssignUser")
	private String assignUser;

	@Column(name = "UploadDate")
	private String uploadDate = getTodayDate();

	@Column(name = "DeleteDate")
	private String deleteDate = getDeleteDateForDB();

	public long getSno() {
		return sno;
	}

	public void setSno(long sno) {
		this.sno = sno;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDistricts() {
		return districts;
	}

	public void setDistricts(String districts) {
		this.districts = districts;
	}

	public String getAssignUser() {
		return assignUser;
	}

	public void setAssignUser(String assignUser) {
		this.assignUser = assignUser;
	}

	public String getSubDistrict() {
		return subDistrict;
	}

	public void setSubDistrict(String subDistrict) {
		this.subDistrict = subDistrict;
	}

	private String getTodayDate() {
		return (new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(String deleteDate) {
		this.deleteDate = deleteDate;
	}

	@SuppressWarnings("deprecation")
	public String getDeleteDateForDB() {

		Date date = new Date();
		date.setDate(date.getDate() + 7);
		return (new SimpleDateFormat("dd-MM-yyyy").format(date));
	}
}
