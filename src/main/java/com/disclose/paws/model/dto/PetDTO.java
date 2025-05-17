package com.disclose.paws.model.dto;

import com.disclose.paws.model.enums.PetSizeEnum;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class PetDTO {
    private Long id;
    private String name;
    private Long age;
    private String gender;
    private String breed;
    private PetSizeEnum size;
    private Boolean castrated;
    private Boolean dewormed;
    private Boolean vaccinated;
    private String description;
    private String imageUrl;
    private Set<Long> sponsorIds;
}