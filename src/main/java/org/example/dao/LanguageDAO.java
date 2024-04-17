package org.example.dao;

import org.example.domain.City;
import org.example.domain.Language;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class LanguageDAO extends GenericDAO<Language>{

    public LanguageDAO(SessionFactory sessionFactory) {
        super(Language.class, sessionFactory);
    }

    public Language getByName(String name) {
        try {
            getCurrentSession().beginTransaction();
            Query<Language> query = getCurrentSession().createQuery("select l from Language l where l.name = :NAME", Language.class);
            query.setParameter("NAME", name);
            query.setMaxResults(1);

            return query.getSingleResult();
        }finally {
            getCurrentSession().getTransaction().commit();
        }
    }
}
