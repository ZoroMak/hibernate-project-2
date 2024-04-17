package org.example.dao;

import org.example.domain.Category;
import org.example.domain.Language;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryDAO extends GenericDAO<Category> {
    public CategoryDAO(SessionFactory sessionFactory) {
        super(Category.class, sessionFactory);
    }

    public Set<Category> getSetByName(Set<String> name) {
        try {
            getCurrentSession().beginTransaction();
            Query<Category> query = getCurrentSession().createQuery("select c from Category c where c.name in :NAME", Category.class);
            query.setParameter("NAME", name);

            return new HashSet<>(query.list());
        }finally {
            getCurrentSession().getTransaction().commit();
        }
    }
}
