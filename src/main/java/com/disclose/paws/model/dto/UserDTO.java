package com.disclose.paws.model.dto;

import com.disclose.paws.model.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String password;
    private ContactDTO contact;
    private AddressDTO address;
    private Boolean active;
    private String profileImageUrl;
    private UserRoleEnum role;
    private Set<Long> favoritePetIds;
    private Set<Long> sponsoredPetIds;

}