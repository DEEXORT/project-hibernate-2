package com.javarush;

import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@AllArgsConstructor
public class RepositoryImpl<AbstractEntity extends IEntity> implements Repository<AbstractEntity> {
    protected final SessionCreator sessionCreator;
    private final Class<AbstractEntity> entityClass;

    @Override
    public void save(AbstractEntity entity) {
        Session session = sessionCreator.getSession();
        Transaction tx = session.beginTransaction();
        try {
            if (entity.getId() == null) {
                session.persist(entity);
            } else {
                session.merge(entity);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    @Override
    public void update(AbstractEntity entity) {
        Session session = sessionCreator.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(entity);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(AbstractEntity entity) {
        sessionCreator.getSession().remove(entity);
    }

    @Override
    public AbstractEntity findById(Long id) {
        return sessionCreator.getSession().get(entityClass, id);
    }

    @Override
    public List<AbstractEntity> findAll() {
        TypedQuery<AbstractEntity> query = sessionCreator.getSession()
                .createQuery("from " + entityClass.getSimpleName(), entityClass);
        return query.getResultList();
    }
}
