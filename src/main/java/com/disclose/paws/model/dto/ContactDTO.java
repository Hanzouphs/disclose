package com.disclose.paws.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactDTO {
    private String email;
    private String phoneNumber;
}
