    package com.strelive.dao;

import com.strelive.entities.Stream;
import com.strelive.utils.HibernateFactory;
import com.strelive.utils.ImplementBaseDAO;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

@Stateless
public class StreamDAO extends ImplementBaseDAO<Stream, Long> {
    public StreamDAO() {
        super(Stream.class);
    }

    @Transactional
    public Optional<Stream> findByStreamKey(String streamKey) {
        try (Session session = HibernateFactory.getSession()) {
            Query<Stream> query = session.createQuery("FROM Stream WHERE streamKey = :streamKey", Stream.class);
            query.setParameter("streamKey", streamKey);
            Stream stream = query.uniqueResult();
            return Optional.ofNullable(stream);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}