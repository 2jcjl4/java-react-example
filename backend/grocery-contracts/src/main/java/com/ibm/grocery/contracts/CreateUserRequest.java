package com.ibm.grocery.contracts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank @Size(min = 3, max = 40) String username,
        @NotBlank @Size(max = 120) String fullName,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank String role) {
}
