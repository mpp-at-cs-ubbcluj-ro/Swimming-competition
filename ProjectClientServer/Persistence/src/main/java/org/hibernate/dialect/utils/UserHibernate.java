package org.hibernate.dialect.utils;

import org.example.model.User;
import org.hibernate.Session;
import org.example.repository.IUserRepository;
import org.hibernate.Transaction;

import java.util.List;

public class UserHibernate implements IUserRepository {

    @Override
    public void add(User elem) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(elem);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public User findById(Integer id) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                User foundUser = session.createQuery("from User where id_user = :id_user", User.class).setParameter("id_user", id).uniqueResult();
                tx.commit();
                return foundUser;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }

        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<User> users = session.createQuery("from User", User.class).list();
                tx.commit();
                return users;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }

        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                User foundUser = session.createQuery("from User where email = :email", User.class).setParameter("email", email).uniqueResult();
                tx.commit();
                return foundUser;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }

        }
        return null;
    }

    @Override
    public User findByEmailPassword(String email, String password) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                User foundUser = session.createQuery("from User where email = :email and password = :password", User.class).setParameter("email", email).setParameter("password", password)
                        .uniqueResult();
                tx.commit();
                return foundUser;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }

        }
        return null;
    }

    @Override
    public void delete(User elem) {

    }

    @Override
    public void update(User elem, Integer id) {

    }
}
