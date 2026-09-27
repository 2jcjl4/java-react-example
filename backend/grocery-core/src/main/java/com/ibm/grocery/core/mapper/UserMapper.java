package com.ibm.grocery.core.mapper;

import com.ibm.grocery.contracts.UserDto;
import com.ibm.grocery.domain.AppUser;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserDto toDto(AppUser user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole().name(),
                user.isActive());
    }
}
