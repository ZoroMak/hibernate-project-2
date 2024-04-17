package org.example.dao;

import org.example.domain.Film;
import org.example.domain.Inventory;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class InventoryDAO extends GenericDAO<Inventory> {
    public InventoryDAO(SessionFactory sessionFactory) {
        super(Inventory.class, sessionFactory);
    }

    public Inventory getFilmForRental() {
        try {
            getCurrentSession().beginTransaction();

            Query<Inventory> query = getCurrentSession().createQuery("select i from Inventory i " +
                    "where i.id not in (select r.inventory.id from Rental r) or " +
                    "i.id in (select r.inventory.id from Rental r where r.returnDate is not null )", Inventory.class);

            query.setMaxResults(1);
            return query.getSingleResult();
        }finally {
            getCurrentSession().getTransaction().commit();
        }

    }
}

