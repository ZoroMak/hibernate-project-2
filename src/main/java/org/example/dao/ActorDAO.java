package org.example.dao;

import org.example.domain.Actor;
import org.example.domain.Category;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;

public class ActorDAO extends GenericDAO<Actor>{
    public ActorDAO(SessionFactory sessionFactory) {
        super(Actor.class, sessionFactory);
    }

    public Set<Actor> getSetByName(Set<String> actors) {
        try {
            getCurrentSession().beginTransaction();
            Query<Actor> query = getCurrentSession().createQuery("select a from Actor a where " +
                    "concat(a.firstName, ' ', a.lastName) in :NAME", Actor.class);
            query.setParameter("NAME", actors);

            return new HashSet<>(query.list());
        }finally {
            getCurrentSession().getTransaction().commit();
        }
    }
}
