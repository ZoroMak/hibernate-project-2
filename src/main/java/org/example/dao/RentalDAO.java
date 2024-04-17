package org.example.dao;

import org.example.domain.Rental;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class RentalDAO extends GenericDAO<Rental>{
    public RentalDAO(SessionFactory sessionFactory) {
        super(Rental.class, sessionFactory);
    }

    public Rental getAnyUnreturnedRental() {
        try {
            getCurrentSession().beginTransaction();
            Query<Rental> query = getCurrentSession().createQuery("select r from Rental r where r.returnDate is null ", Rental.class);
            query.setMaxResults(1);
            return query.getSingleResult();
        }finally {
            getCurrentSession().getTransaction().commit();
        }

    }
}
