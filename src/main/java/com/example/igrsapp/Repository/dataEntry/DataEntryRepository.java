package com.example.igrsapp.Repository.dataEntry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.igrsapp.modals.dataEntry.DataEntry;

public interface DataEntryRepository extends JpaRepository<DataEntry, Long> {

	DataEntry findLocationByFileIdAndDistrictsAndSubDistrict(String fileId, String district, String subDistrict);

	String findLocationByFileId(String oldFileName);

	DataEntry findByFileId(String fileId);
}
