package com.javarush.dao;

import com.javarush.entity.Inventory;
import com.javarush.entity.Rental;
import com.javarush.config.SessionCreator;
import com.javarush.entity.Store;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class RentalRepository extends RepositoryImpl<Rental> {
    public RentalRepository(SessionCreator sessionCreator, Class<Rental> rentalClass) {
        super(sessionCreator, rentalClass);
    }

    public Rental getAnyUnreturnedFilm() {
        Session session = sessionCreator.getSession();
        String hql = "select r from Rental r where r.returnDate is null";
        Query<Rental> query = session.createQuery(hql, Rental.class);
        query.setMaxResults(1);
        return query.getSingleResult();
    }

    public Rental findActiveRentalByCustomerAndInventory(Long customerId, Long inventoryId) {
        Session session = sessionCreator.getSession();
        String hql = "select r from Rental r " +
                "where r.customer.id = :customerId " +
                "and r.inventory.id = :inventoryId " +
                "and r.returnDate is null";
        Query<Rental> query = session.createQuery(hql, Rental.class);
        query.setParameter("customerId", customerId);
        query.setParameter("inventoryId", inventoryId);
        return query.uniqueResultOptional().orElse(null);
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
