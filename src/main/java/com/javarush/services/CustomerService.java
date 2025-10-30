package com.javarush;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@AllArgsConstructor
public class CustomerService {
    private SessionCreator sessionCreator;
    private final RepositoryImpl<Customer> customerRepository;
    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final StoreRepository storeRepository;
    private final RentalRepository rentalRepository;

    public void save(Customer customer) {
        Session session = sessionCreator.getSession();
        Transaction tx = session.beginTransaction();
        try {
            City city = cityRepository.findOrCreate(customer.getAddress().getCity(), session); // find city in database
            customer.getAddress().setCity(city); // update Address if city exists in database

            Address address = addressRepository.findOrCreate(customer.getAddress(), session);  // find address in database
            customer.setAddress(address); // update Store if address exists in database

            Store store = storeRepository.findOrCreate(customer.getStore(), session);  // find store in database
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

    public Rental getById(Long id) {
        return rentalRepository.findById(id);
    }
}
