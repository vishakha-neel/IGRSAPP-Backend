package com.example.igrsapp.Repository.mainigrs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("DatFetchRepository")
@Transactional
public class DataFetchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public String getLocation(String autoFileNo, String district, String type) {
        // NOTE: Validate 'district' if it's used directly to prevent SQL injection
        String sql = "SELECT location FROM " + district + " WHERE FileId = :fileId AND subdistrict = :type";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("fileId", autoFileNo);
        query.setParameter("type", type);

        Object result = query.getResultStream().findFirst().orElse("");
        return result != null ? result.toString() : "";
    }
}
