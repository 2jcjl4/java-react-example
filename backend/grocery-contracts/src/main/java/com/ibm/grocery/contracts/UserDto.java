package com.ibm.grocery.contracts;

public record UserDto(Long id, String username, String fullName, String role, boolean active) {
}
