package org.example.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class GenericDAO<T> {
    private final Class<T> clazz;
    private final SessionFactory sessionFactory;

    public GenericDAO(final Class<T> clazzToSet, SessionFactory sessionFactory)   {
        this.clazz = clazzToSet;
        this.sessionFactory = sessionFactory;
    }

    public T getById(final long id) {
        return (T) getCurrentSession().get(clazz, id);
    }

    public List<T> getItems(int offset, int count) {
        try {
            getCurrentSession().beginTransaction();
            Query<T> query = getCurrentSession().createQuery("from " + clazz.getName(), clazz);
            query.setFirstResult(offset);
            query.setMaxResults(count);
            return query.list();
        }finally {
            getCurrentSession().getTransaction().commit();
        }
    }

    public List<T> findAll() {
        try(Session session = getCurrentSession()) {
            session.beginTransaction();
            session.getTransaction().commit();
            return getCurrentSession().createQuery("from " + clazz.getName(), clazz).list();
        }
    }

    public T save(final T entity) {
        try {
            getCurrentSession().beginTransaction();
            getCurrentSession().saveOrUpdate(entity);
            return entity;
        }finally {
            getCurrentSession().getTransaction().commit();
        }
    }

    public T update(final T entity) {
        return (T) getCurrentSession().merge(entity);
    }

    public void delete(final T entity) {
        getCurrentSession().delete(entity);
    }

    public void deleteById(final long entityId) {
        final T entity = getById(entityId);
        delete(entity);
    }

    protected Session getCurrentSession() {
        sessionFactory.openSession();
        return sessionFactory.getCurrentSession();
    }
}
