package com.ibm.grocery.core.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordHasherTest {

    private final PasswordHasher hasher = new PasswordHasher();

    @Test
    void acceptsTheCorrectPassword() {
        assertTrue(hasher.matches("Correct123!", hasher.hash("Correct123!")));
    }

    @Test
    void rejectsAnIncorrectPassword() {
        assertFalse(hasher.matches("Wrong123!", hasher.hash("Correct123!")));
    }

    @Test
    void producesADifferentHashForTheSamePassword() {
        assertNotEquals(hasher.hash("Correct123!"), hasher.hash("Correct123!"));
    }
}
