package com.ibm.grocery.core.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Object identifier) {
        super(resource + " " + identifier + " was not found");
    }
}
