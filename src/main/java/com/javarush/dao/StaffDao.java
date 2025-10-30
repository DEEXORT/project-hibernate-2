package com.javarush.dao;

import com.javarush.config.SessionCreator;
import com.javarush.entity.Staff;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class StaffRepository extends RepositoryImpl<Staff> {
    public StaffRepository(SessionCreator sessionCreator, Class<Staff> staffClass) {
        super(sessionCreator, staffClass);
    }


    public Staff getAnyStaffFromStore(Long storeId) {
        Session session = sessionCreator.getSession();
        String hql = "FROM Staff s WHERE s.store.id = :id";
        Query<Staff> query = session.createQuery(hql, Staff.class);
        query.setParameter("id", storeId);
        query.setMaxResults(1);
        return query.uniqueResult();
    }
}
