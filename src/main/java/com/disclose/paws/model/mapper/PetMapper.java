package com.disclose.paws.model.mapper;

import com.disclose.paws.model.PetEntity;
import com.disclose.paws.model.UserEntity;
import com.disclose.paws.model.dto.PetDTO;
import com.disclose.paws.model.enums.PetSizeEnum;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PetMapper {

    @Autowired
    protected EntityReferenceMapper referenceMapper;

    @Mapping(target = "size", source = "size", qualifiedByName = "stringToPetSizeEnum")
    @Mapping(target = "sponsorIds", source = "sponsors", qualifiedByName = "sponsorsToIds")
    public abstract PetDTO toDto(PetEntity entity);

    @Mapping(target = "size", source = "size", qualifiedByName = "petSizeEnumToString")
    @Mapping(target = "sponsors", ignore = true)
    public abstract PetEntity toEntity(PetDTO dto);

    @Named("sponsorsToIds")
    protected Set<Long> sponsorsToIds(Set<UserEntity> sponsors) {
        if (sponsors == null) {
            return null;
        }
        return sponsors.stream()
                .map(UserEntity::getId)
                .collect(Collectors.toSet());
    }

    public PetEntity toPetEntityWithSponsors(PetDTO dto) {
        PetEntity entity = toEntity(dto);
        if (dto.getSponsorIds() != null && !dto.getSponsorIds().isEmpty()) {
            entity.setSponsors(referenceMapper.mapUserIdsToUsers(dto.getSponsorIds()));
        }
        return entity;
    }

    @Named("stringToPetSizeEnum")
    PetSizeEnum stringToPetSizeEnum(String size) {
        if (size == null) {
            return null;
        }

        for (PetSizeEnum sizeEnum : PetSizeEnum.values()) {
            if (sizeEnum.name().equals(size)) {
                return sizeEnum;
            }
        }

        return null;
    }

    @Named("petSizeEnumToString")
    public String petSizeEnumToString(PetSizeEnum sizeEnum) {
        return sizeEnum != null ? sizeEnum.name() : null;
    }
}