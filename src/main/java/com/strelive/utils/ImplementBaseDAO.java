package com.strelive.utils;

import jakarta.transaction.Transactional;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class ImplementBaseDAO<T, ID> implements BaseDAO<T, ID> {
    private final Class<T> classType;

    public ImplementBaseDAO(Class<T> classType) {
        this.classType = classType;
    }

    @Override
    public Optional<T> findById(ID id) {
        try (Session session = HibernateFactory.getSession()) {
            T findingObject = session.find(classType, id);
            return Optional.ofNullable(findingObject);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateFactory.getSession()) {
            return session.createQuery("FROM " + classType.getName(), classType).getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Transactional
    @Override
    public T save(T entity) {
        try (Session session = HibernateFactory.getSession()) {
            session.persist(entity);
            return entity;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Override
    public T update(T entity) {
        try (Session session = HibernateFactory.getSession()) {
            session.merge(entity);
            session.flush();
            return entity;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Override
    public void delete(ID id) {
        Session session = HibernateFactory.getSession();
        Optional<T> deleteEntity = findById(id);
        deleteEntity.ifPresent(session::remove);
    }
}
