package com.disclose.paws.service;

import com.disclose.paws.exception.ResourceNotFoundException;
import com.disclose.paws.model.PetEntity;
import com.disclose.paws.model.dto.PetDTO;
import com.disclose.paws.model.mapper.PetMapper;
import com.disclose.paws.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.disclose.paws.utils.Utils.getValueOrDefault;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Transactional
    public PetDTO createPet(PetDTO petDTO) {
        log.info("Creating new pet: {}", petDTO.getName());
        if (petDTO.getName() == null || petDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Pet name cannot be empty");
        }

        PetEntity petEntity = petMapper.toPetEntityWithSponsors(petDTO);
        PetEntity savedPet = petRepository.save(petEntity);
        log.info("Pet created with ID: {}", savedPet.getId());
        return petMapper.toDto(savedPet);
    }

    public List<PetDTO> getAllPets() {
        log.info("Retrieving all pets");
        List<PetEntity> pets = petRepository.findAll();
        log.info("Found {} pets", pets.size());
        return pets.stream()
                .map(petMapper::toDto)
                .collect(Collectors.toList());
    }

    public PetDTO getPetById(Long id) {
        log.info("Finding pet with ID: {}", id);
        PetEntity petEntity = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with ID: " + id));
        log.info("Pet found: {}", petEntity.getName());
        return petMapper.toDto(petEntity);
    }

    @Transactional
    public void deletePet(Long id) {
        log.info("Deleting pet with ID: {}", id);
        PetEntity petToDelete = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with ID: " + id));
        petRepository.delete(petToDelete);
        log.info("Pet successfully deleted");
    }

    @Transactional
    public PetDTO updatePet(PetDTO petDTO) {
        log.info("Updating pet with ID: {}", petDTO.getId());
        if (petDTO.getId() == null) {
            throw new IllegalArgumentException("Pet ID cannot be null");
        }

        PetEntity petToUpdate = petRepository.findById(petDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with ID: " + petDTO.getId()));

        PetEntity updatedEntity = petMapper.toPetEntityWithSponsors(petDTO);
        updatedEntity.setVersion(petToUpdate.getVersion());

        PetEntity savedEntity = petRepository.save(updatedEntity);
        log.info("Pet successfully updated: {}", savedEntity.getName());
        return petMapper.toDto(savedEntity);
    }


    public Page<PetDTO> findPets(String name, String breed, Long age, String gender,
                                 String size, Boolean castrated, Boolean dewormed,
                                 Boolean vaccinated, String description, Pageable pageable) {

        log.info("Searching pets with filters: name={}, breed={}, age={}, gender={}, size={}, castrated={}, dewormed={}, vaccinated={}, description={}",
                getValueOrDefault(name), getValueOrDefault(breed), getValueOrDefault(age),
                getValueOrDefault(gender), getValueOrDefault(size), getValueOrDefault(castrated),
                getValueOrDefault(dewormed), getValueOrDefault(vaccinated), getValueOrDefault(description));

        Page<PetEntity> petEntities = petRepository.findAll(createPetSpecification(
                        name, breed, age, gender, size, castrated, dewormed, vaccinated, description),
                pageable);

        log.info("Found {} pets in current page", petEntities.getNumberOfElements());
        return petEntities.map(petMapper::toDto);
    }

    private org.springframework.data.jpa.domain.Specification<PetEntity> createPetSpecification(
            String name, String breed, Long age, String gender,
            String size, Boolean castrated, Boolean dewormed,
            Boolean vaccinated, String description) {

        return (root, query, criteriaBuilder) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            addLikePredicateIfNotEmpty(predicates, root, criteriaBuilder, "name", name);
            addLikePredicateIfNotEmpty(predicates, root, criteriaBuilder, "breed", breed);
            addLikePredicateIfNotEmpty(predicates, root, criteriaBuilder, "gender", gender);
            addLikePredicateIfNotEmpty(predicates, root, criteriaBuilder, "description", description);

            if (age != null) {
                predicates.add(criteriaBuilder.equal(root.get("age"), age));
            }

            if (size != null && !size.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("size").as(String.class)),
                        size.toLowerCase().trim()));
            }

            addBooleanPredicateIfNotNull(predicates, root, criteriaBuilder, "castrated", castrated);
            addBooleanPredicateIfNotNull(predicates, root, criteriaBuilder, "dewormed", dewormed);
            addBooleanPredicateIfNotNull(predicates, root, criteriaBuilder, "vaccinated", vaccinated);

            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    private void addLikePredicateIfNotEmpty(
            java.util.List<jakarta.persistence.criteria.Predicate> predicates,
            jakarta.persistence.criteria.Root<PetEntity> root,
            jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
            String fieldName, String value) {

        if (value != null && !value.trim().isEmpty()) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(fieldName)),
                    "%" + value.toLowerCase().trim() + "%"));
        }
    }

    private void addBooleanPredicateIfNotNull(
            java.util.List<jakarta.persistence.criteria.Predicate> predicates,
            jakarta.persistence.criteria.Root<PetEntity> root,
            jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
            String fieldName, Boolean value) {

        if (value != null) {
            predicates.add(criteriaBuilder.equal(root.get(fieldName), value));
        }
    }
}