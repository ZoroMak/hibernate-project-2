package org.example.dao;

import org.example.domain.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class CityDAO extends GenericDAO<City> {
    public CityDAO(SessionFactory sessionFactory) {
        super(City.class, sessionFactory);
    }

    public City getByName(String name) {
        try {
            getCurrentSession().beginTransaction();
            Query<City> query = getCurrentSession().createQuery("select c from City c where c.city = :NAME", City.class);
            query.setParameter("NAME", name);
            query.setMaxResults(1);

            return query.getSingleResult();
        }finally {
            getCurrentSession().getTransaction().commit();
        }
    }
}
