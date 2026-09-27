package com.ibm.grocery.contracts;

public record LoginResponse(String token, long expiresInSeconds, UserDto user) {
}
