package com.disclose.paws.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PetSizeEnum {

    NORMAL_SIZE("NORMAL"),
    MEDIUM_SIZE("MEDIUM"),
    LARGE_SIZE("LARGE"),;

    @JsonValue
    private final String description;
}
