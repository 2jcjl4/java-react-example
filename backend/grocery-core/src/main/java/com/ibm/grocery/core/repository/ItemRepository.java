package com.ibm.grocery.core.repository;

import com.ibm.grocery.domain.Item;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Singleton
public class ItemRepository {

    @PersistenceContext(unitName = "groceryPU")
    private EntityManager entityManager;

    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Item.class, id));
    }

    /** Locks the row so concurrent sales cannot oversell the same item. */
    public Optional<Item> findByIdForUpdate(Long id) {
        return Optional.ofNullable(entityManager.find(Item.class, id, LockModeType.PESSIMISTIC_WRITE));
    }

    public Optional<Item> findBySku(String sku) {
        return entityManager
                .createQuery("select i from Item i where lower(i.sku) = lower(:sku)", Item.class)
                .setParameter("sku", sku)
                .getResultStream()
                .findFirst();
    }

    public List<Item> search(String term, boolean includeInactive) {
        boolean filtered = term != null && !term.isBlank();
        StringBuilder jpql = new StringBuilder("select i from Item i where 1 = 1");
        if (!includeInactive) {
            jpql.append(" and i.active = true");
        }
        if (filtered) {
            jpql.append(" and (lower(i.name) like :pattern or lower(i.sku) like :pattern")
                    .append(" or lower(i.category) like :pattern)");
        }
        jpql.append(" order by i.name");

        TypedQuery<Item> query = entityManager.createQuery(jpql.toString(), Item.class);
        if (filtered) {
            query.setParameter("pattern", "%" + term.toLowerCase() + "%");
        }
        return query.getResultList();
    }

    public List<Item> findLowStock() {
        return entityManager
                .createQuery("select i from Item i where i.active = true and i.quantityOnHand <= i.reorderLevel "
                        + "order by i.quantityOnHand", Item.class)
                .getResultList();
    }

    public long countActive() {
        return entityManager
                .createQuery("select count(i) from Item i where i.active = true", Long.class)
                .getSingleResult();
    }

    public long totalUnitsOnHand() {
        Long total = entityManager
                .createQuery("select coalesce(sum(i.quantityOnHand), 0) from Item i where i.active = true", Long.class)
                .getSingleResult();
        return total == null ? 0L : total;
    }

    public BigDecimal stockValue() {
        BigDecimal value = entityManager
                .createQuery("select coalesce(sum(i.unitPrice * i.quantityOnHand), 0) from Item i where i.active = true",
                        BigDecimal.class)
                .getSingleResult();
        return value == null ? BigDecimal.ZERO : value;
    }

    public Item save(Item item) {
        if (item.getId() == null) {
            entityManager.persist(item);
            return item;
        }
        return entityManager.merge(item);
    }
}
