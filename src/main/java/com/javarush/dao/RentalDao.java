package com.javarush.dao;

import com.javarush.entity.Inventory;
import com.javarush.entity.Rental;
import com.javarush.config.SessionCreator;
import com.javarush.entity.Store;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class RentalDao extends RepositoryImpl<Rental> {
    public RentalDao(SessionCreator sessionCreator, Class<Rental> rentalClass) {
        super(sessionCreator, rentalClass);
    }

    public Inventory getAnyInventoryWithAvailableFilm(Store store) {
        Session session = sessionCreator.getSession();
        String hql = "from Inventory i join Film f on i.film.id = f.id " +
                "where i.id not in (select r.inventory.id from Rental r where r.returnDate is null " +
                "and r.inventory.id = i.id)" +
                "and i.store.id = :storeId";
        try {
            Query<Inventory> query = session.createQuery(hql, Inventory.class);
            query.setParameter("storeId", store.getId());
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("No Film found: ", e);
        }
    }
}
