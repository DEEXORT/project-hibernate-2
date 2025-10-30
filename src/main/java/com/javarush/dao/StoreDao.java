package com.javarush.dao;

import com.javarush.config.SessionCreator;
import com.javarush.entity.Store;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class StoreDao extends RepositoryImpl<Store> {
    public StoreDao(SessionCreator sessionCreator, Class<Store> storeClass) {
        super(sessionCreator, storeClass);
    }

    public Store findOrCreate(Store store, Session session) {
        String hql = "from Store s where s.address.id = :addressId and s.managerStaff.id = :manager_staff_id";
        Query<Store> query = session.createQuery(hql, Store.class);
        query.setParameter("addressId", store.getAddress().getId());
        query.setParameter("manager_staff_id", store.getManagerStaff().getId());
        List<Store> stores = query.list();
        if (!stores.isEmpty()) {
            return stores.get(0);
        } else {
            this.save(store);
            return store;
        }
    }
}
