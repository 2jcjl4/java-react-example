package com.ibm.grocery.core.repository;

import com.ibm.grocery.domain.AppUser;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserRepository {

    @PersistenceContext(unitName = "groceryPU")
    private EntityManager entityManager;

    public Optional<AppUser> findByUsername(String username) {
        return entityManager
                .createQuery("select u from AppUser u where lower(u.username) = lower(:username)", AppUser.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    public Optional<AppUser> findById(Long id) {
        return Optional.ofNullable(entityManager.find(AppUser.class, id));
    }

    public List<AppUser> findAll() {
        return entityManager
                .createQuery("select u from AppUser u order by u.username", AppUser.class)
                .getResultList();
    }

    public long count() {
        return entityManager.createQuery("select count(u) from AppUser u", Long.class).getSingleResult();
    }

    public AppUser save(AppUser user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        }
        return entityManager.merge(user);
    }
}
