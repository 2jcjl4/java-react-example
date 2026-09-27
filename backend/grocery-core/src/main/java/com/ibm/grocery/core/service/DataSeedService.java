package com.ibm.grocery.core.service;

import com.ibm.grocery.core.repository.ItemRepository;
import com.ibm.grocery.core.repository.UserRepository;
import com.ibm.grocery.core.security.PasswordHasher;
import com.ibm.grocery.domain.AppUser;
import com.ibm.grocery.domain.Item;
import com.ibm.grocery.domain.Role;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/** Creates the first administrator and a small catalogue so a fresh database is usable. */
@ApplicationScoped
public class DataSeedService {

    private static final Logger LOGGER = Logger.getLogger(DataSeedService.class.getName());

    @Inject
    UserRepository userRepository;

    @Inject
    ItemRepository itemRepository;

    @Inject
    PasswordHasher passwordHasher;

    @Inject
    @ConfigProperty(name = "SEED_ADMIN_USERNAME", defaultValue = "admin")
    String adminUsername;

    @Inject
    @ConfigProperty(name = "SEED_ADMIN_PASSWORD", defaultValue = "Admin123!")
    String adminPassword;

    @Transactional
    public void seed() {
        if (userRepository.count() == 0) {
            AppUser admin = new AppUser();
            admin.setUsername(adminUsername.toLowerCase());
            admin.setFullName("Store Administrator");
            admin.setRole(Role.ADMIN);
            admin.setPasswordHash(passwordHasher.hash(adminPassword));
            userRepository.save(admin);
            LOGGER.info("Seeded initial administrator account: " + admin.getUsername());
        }

        if (itemRepository.countActive() == 0) {
            defaultCatalogue().forEach(itemRepository::save);
            LOGGER.info("Seeded the starter item catalogue");
        }
    }

    private List<Item> defaultCatalogue() {
        return List.of(
                item("GRO-001", "Whole Milk 2L", "Dairy", "2.19", 40, 12),
                item("GRO-002", "Free Range Eggs 12", "Dairy", "3.49", 25, 10),
                item("GRO-003", "Wholemeal Bread", "Bakery", "1.35", 18, 8),
                item("GRO-004", "Bananas 1kg", "Produce", "1.10", 60, 20),
                item("GRO-005", "Baked Beans 400g", "Ambient", "0.89", 120, 30),
                item("GRO-006", "Chicken Breast 500g", "Meat", "5.25", 9, 10));
    }

    private Item item(String sku, String name, String category, String price, int quantity, int reorderLevel) {
        Item item = new Item();
        item.setSku(sku);
        item.setName(name);
        item.setCategory(category);
        item.setUnitPrice(new BigDecimal(price));
        item.setQuantityOnHand(quantity);
        item.setReorderLevel(reorderLevel);
        return item;
    }
}
