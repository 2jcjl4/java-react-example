package com.ibm.grocery.core.repository;

import com.ibm.grocery.domain.Sale;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Singleton
public class SaleRepository {

    @PersistenceContext(unitName = "groceryPU")
    private EntityManager entityManager;

    public Sale save(Sale sale) {
        entityManager.persist(sale);
        return sale;
    }

    public Optional<Sale> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Sale.class, id));
    }

    public List<Sale> findRecent(int limit) {
        return entityManager
                .createQuery("select s from Sale s order by s.soldAt desc, s.id desc", Sale.class)
                .setMaxResults(limit)
                .getResultList();
    }

    public long countSince(Instant since) {
        return entityManager
                .createQuery("select count(s) from Sale s where s.soldAt >= :since", Long.class)
                .setParameter("since", since)
                .getSingleResult();
    }

    public BigDecimal revenueSince(Instant since) {
        BigDecimal value = entityManager
                .createQuery("select coalesce(sum(s.totalAmount), 0) from Sale s where s.soldAt >= :since",
                        BigDecimal.class)
                .setParameter("since", since)
                .getSingleResult();
        return value == null ? BigDecimal.ZERO : value;
    }
}
