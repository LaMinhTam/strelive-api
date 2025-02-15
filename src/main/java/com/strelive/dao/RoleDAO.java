package com.strelive.dao;

import com.strelive.entities.Role;
import com.strelive.entities.User;
import com.strelive.utils.HibernateFactory;
import com.strelive.utils.ImplementBaseDAO;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

@Stateless
public class RoleDAO extends ImplementBaseDAO<Role, Long> {
    public RoleDAO() {
        super(Role.class);
    }

    @Transactional
    public Optional<Role> findByName(String name) {
        try (Session session = HibernateFactory.getSession()) {
            Query<Role> query = session.createQuery("FROM Role WHERE name = :name", Role.class);
            query.setParameter("name", name);
            Role role = query.uniqueResult();
            return Optional.ofNullable(role);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
