package com.strelive.dao;

import com.strelive.entities.User;
import com.strelive.utils.HibernateFactory;
import com.strelive.utils.ImplementBaseDAO;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

@Stateless
public class UserDAO extends ImplementBaseDAO<User, Long> {

    public UserDAO() {
        super(User.class);
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateFactory.getSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            User user = query.uniqueResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional
    public User merge(User user) {
        try (Session session = HibernateFactory.getSession()) {
            session.merge(user);
            session.flush();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public User saveOrUpdate(User user) {
        try (Session session = HibernateFactory.getSession()) {
            session.beginTransaction();
            session.saveOrUpdate(user);
            session.flush();
            session.getTransaction().commit();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}