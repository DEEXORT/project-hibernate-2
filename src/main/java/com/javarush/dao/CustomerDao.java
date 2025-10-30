package com.javarush.dao;

import com.javarush.config.SessionCreator;
import com.javarush.entity.Customer;

public class CustomerDao extends RepositoryImpl<Customer> {
    public CustomerDao(SessionCreator sessionCreator, Class<Customer> customerClass) {
        super(sessionCreator, customerClass);
    }
}
