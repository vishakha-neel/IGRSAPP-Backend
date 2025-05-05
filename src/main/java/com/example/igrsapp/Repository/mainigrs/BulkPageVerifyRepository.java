package com.example.igrsapp.Repository.mainigrs;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.igrsapp.modals.mainigrs.BulkPageVerify;

@Repository("BulkPageVerifyRepository")
@Transactional
public class BulkPageVerifyRepository {
	
	 @PersistenceContext
	 private EntityManager entityManager;
	 
	 public void save(BulkPageVerify verify) {
	        entityManager.persist(verify); // Correct way to save using JPA EntityManager
     }
	 
	 public BulkPageVerify findByFileId(String fileId) {
		    String query = "FROM BulkPageVerify WHERE fileId = :fileId and status='P'";
		    try {
		    	List<BulkPageVerify> data= entityManager.createQuery(query, BulkPageVerify.class)
		    		    .setParameter("fileId", fileId)
		    		    .getResultList();
		    	return data.isEmpty()?null:data.get(0);
		    } catch (NoResultException e) {
		        return null; // or handle as per your application's logic
		    }
		}

}