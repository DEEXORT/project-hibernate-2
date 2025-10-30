package com.javarush.dao;

import com.javarush.entity.City;
import com.javarush.config.SessionCreator;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CityDao extends RepositoryImpl<City> {

    public CityDao(SessionCreator sessionCreator, Class<City> cityClass) {
        super(sessionCreator, cityClass);
    }

    public City findOrCreate(City city, Session session) {
        String hql = "from City c where c.country.country = :countryName and c.city = :cityName";
        Query<City> query = session.createQuery(hql, City.class);
        query.setParameter("countryName", city.getCountry().getCountry());
        query.setParameter("cityName", city.getCity());
        List<City> cities = query.list();
        if (!cities.isEmpty()) {
            return cities.get(0);
        } else {
            this.save(city);
            return city;
        }
    }
}
