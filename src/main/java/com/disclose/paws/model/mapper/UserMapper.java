package com.disclose.paws.model.mapper;


import com.disclose.paws.model.PetEntity;
import com.disclose.paws.model.UserEntity;
import com.disclose.paws.model.dto.UserDTO;
import com.disclose.paws.model.enums.UserRoleEnum;
import com.disclose.paws.utils.Utils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Utils.class})
public abstract class UserMapper {

    @Autowired
    private EntityReferenceMapper entityReferenceMapper;

    @Mapping(target = "address", expression = "java(Utils.createAddressFrom(" +
            "entity.getStreet(), entity.getCity(), entity.getState(), " +
            "entity.getCountry(), entity.getPostalCode()))")
    @Mapping(target = "contact", expression = "java(Utils.createContactFrom(" +
            "entity.getEmail(), entity.getPhoneNumber()))")
    @Mapping(target = "favoritePetIds", source = "favoritePets", qualifiedByName = "petsToIds")
    @Mapping(target = "sponsoredPetIds", source = "sponsoredPets", qualifiedByName = "petsToIds")
    @Mapping(target = "role", source = "role", qualifiedByName = "stringToUserRoleEnum")
    public abstract UserDTO toDto(UserEntity entity);

    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "state", source = "address.state")
    @Mapping(target = "country", source = "address.country")
    @Mapping(target = "postalCode", source = "address.postalCode")
    @Mapping(target = "email", source = "contact.email")
    @Mapping(target = "phoneNumber", source = "contact.phoneNumber")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "favoritePets", ignore = true)
    @Mapping(target = "sponsoredPets", ignore = true)
    @Mapping(target = "role", source = "role", qualifiedByName = "userRoleEnumToString")
    public abstract UserEntity toEntity(UserDTO dto);

    @Named("stringToUserRoleEnum")
    UserRoleEnum stringToUserRoleEnum(String role) {
        if (role == null) {
            return null;
        }

        for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
            if (roleEnum.name().equals(role)) {
                return roleEnum;
            }
        }

        return null;
    }

    @Named("userRoleEnumToString")
    public String userRoleEnumToString(UserRoleEnum roleEnum) {
        return roleEnum != null ? roleEnum.name() : null;
    }

    @Named("petsToIds")
    public Set<Long> petsToIds(Set<PetEntity> pets) {
        if (pets == null) {
            return null;
        }
        return pets.stream()
                .map(PetEntity::getId)
                .collect(Collectors.toSet());
    }

    @AfterMapping
    protected void afterToUserEntity(@MappingTarget UserEntity entity, UserDTO dto) {
        if (dto.getFavoritePetIds() != null) {
            entity.setFavoritePets(entityReferenceMapper.mapPetIdsToEntities(dto.getFavoritePetIds()));
        }

        if (dto.getSponsoredPetIds() != null) {
            entity.setSponsoredPets(entityReferenceMapper.mapPetIdsToEntities(dto.getSponsoredPetIds()));
        }
    }

}