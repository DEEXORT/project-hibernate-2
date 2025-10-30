package com.javarush.dao;

import com.javarush.entity.Address;
import com.javarush.config.SessionCreator;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class AddressDao extends RepositoryImpl<Address> {
    public AddressDao(SessionCreator sessionCreator, Class<Address> addressClass) {
        super(sessionCreator, addressClass);
    }

    public Address findOrCreate(Address address, Session session) {
        String hql = "from Address a where a.address = :address and a.city.id = :cityId";
        Query<Address> query = session.createQuery(hql, Address.class);
        query.setParameter("address", address.getAddress());
        query.setParameter("cityId", address.getCity().getId());
        List<Address> addresses = query.list();
        if (!addresses.isEmpty()) {
            return addresses.get(0);
        } else {
            this.save(address);
            return address;
        }
    }
}
