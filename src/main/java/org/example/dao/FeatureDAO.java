package org.example.dao;

import org.example.domain.Feature;
import org.hibernate.SessionFactory;

public class FeatureDAO extends GenericDAO<Feature> {


    public FeatureDAO(SessionFactory sessionFactory) {
        super(Feature.class, sessionFactory);
    }
}
