package org.example.dao;

import org.example.domain.Rating;
import org.hibernate.SessionFactory;

public class RatingDAO extends GenericDAO<Rating>{
    public RatingDAO(SessionFactory sessionFactory) {
        super(Rating.class, sessionFactory);
    }
}
