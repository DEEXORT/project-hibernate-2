package com.javarush.services;

import com.javarush.config.SessionCreator;
import com.javarush.dao.AddressDao;
import com.javarush.dao.CityDao;
import com.javarush.dao.CustomerDao;
import com.javarush.dao.StoreDao;
import com.javarush.entity.Address;
import com.javarush.entity.City;
import com.javarush.entity.Customer;
import com.javarush.entity.Store;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@AllArgsConstructor
public class CustomerService {
    private SessionCreator sessionCreator;
    private final CustomerDao customerRepository;
    private final AddressDao addressDao;
    private final CityDao cityDao;
    private final StoreDao storeDao;

    public void save(Customer customer) {
        Session session = sessionCreator.getSession();
        Transaction tx = session.beginTransaction();
        try {
            City city = cityDao.findOrCreate(customer.getAddress().getCity(), session); // find city in database
            customer.getAddress().setCity(city); // update Address if city exists in database

            Address address = addressDao.findOrCreate(customer.getAddress(), session);  // find address in database
            customer.setAddress(address); // update Store if address exists in database

            Store store = storeDao.findOrCreate(customer.getStore(), session);  // find store in database
            customer.setStore(store); // update Customer if store exists in database

            customerRepository.save(customer);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }
}
