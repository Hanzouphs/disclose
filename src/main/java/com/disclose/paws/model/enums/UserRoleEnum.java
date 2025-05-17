package com.disclose.paws.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoleEnum {
    NORMAL_USER_ROLE("NORMAL"),
    OPERATOR_USER_ROLE(" OPERATOR"),
    ADMIN_USER_ROLE("ADMIN"),;

    @JsonValue
    private final String description;
}