package com.disclose.paws.model.mapper;


import com.disclose.paws.model.PetEntity;
import com.disclose.paws.model.UserEntity;
import com.disclose.paws.repository.PetRepository;
import com.disclose.paws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EntityReferenceMapper {

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private UserRepository userRepository;

    public Set<PetEntity> mapPetIdsToPets(Set<Long> ids) {
        if (ids == null) return new HashSet<>();
        return ids.stream()
                .map(id -> petRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Set<UserEntity> mapUserIdsToUsers(Set<Long> ids) {
        if (ids == null) return new HashSet<>();
        return ids.stream()
                .map(id -> userRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Set<PetEntity> mapPetIdsToEntities(Set<Long> petIds) {
        if (petIds == null || petIds.isEmpty()) {
            return new HashSet<>();
        }

        return new HashSet<>(petRepository.findAllById(petIds));
    }

}