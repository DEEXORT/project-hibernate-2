package com.javarush.config;

import com.javarush.entity.Actor;
import com.javarush.entity.Address;
import com.javarush.entity.Category;
import com.javarush.entity.City;
import com.javarush.entity.Country;
import com.javarush.entity.Customer;
import com.javarush.entity.Film;
import com.javarush.entity.FilmText;
import com.javarush.entity.Inventory;
import com.javarush.entity.Language;
import com.javarush.entity.Payment;
import com.javarush.entity.Rental;
import com.javarush.entity.Staff;
import com.javarush.entity.Store;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionCreator {
    private final SessionFactory sessionFactory;

    public SessionCreator() {
        sessionFactory = new Configuration().configure()
                .addAnnotatedClass(Film.class)
                .addAnnotatedClass(FilmText.class)
                .addAnnotatedClass(Language.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Store.class)
                .addAnnotatedClass(Staff.class)
                .addAnnotatedClass(Rental.class)
                .addAnnotatedClass(Inventory.class)
                .addAnnotatedClass(Payment.class)
                .buildSessionFactory();
    }
    public Session getSession() {
        return sessionFactory.openSession();
    }

    public void close() {
        sessionFactory.close();
    }
}
