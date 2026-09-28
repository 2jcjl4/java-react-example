package com.ibm.grocery.micronaut;

import io.micronaut.runtime.Micronaut;

public final class GroceryMicronautApplication {

    private GroceryMicronautApplication() {
    }

    public static void main(String[] args) {
        Micronaut.run(GroceryMicronautApplication.class, args);
    }
}
