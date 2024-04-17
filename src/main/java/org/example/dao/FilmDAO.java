package org.example.dao;

import org.example.domain.Film;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class FilmDAO extends GenericDAO<Film> {

    public FilmDAO(SessionFactory sessionFactory) {
        super(Film.class, sessionFactory);
    }
}
