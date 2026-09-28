package com.ibm.grocery.core.repository;

import com.ibm.grocery.domain.StockMovement;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Singleton
public class StockMovementRepository {

    @PersistenceContext(unitName = "groceryPU")
    private EntityManager entityManager;

    public void record(StockMovement movement) {
        entityManager.persist(movement);
    }

    public List<StockMovement> findRecent(Long itemId, int limit) {
        String jpql = itemId == null
                ? "select m from StockMovement m order by m.occurredAt desc, m.id desc"
                : "select m from StockMovement m where m.item.id = :itemId order by m.occurredAt desc, m.id desc";

        TypedQuery<StockMovement> query = entityManager.createQuery(jpql, StockMovement.class)
                .setMaxResults(limit);
        if (itemId != null) {
            query.setParameter("itemId", itemId);
        }
        return query.getResultList();
    }
}
