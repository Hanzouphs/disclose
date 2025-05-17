package com.disclose.paws.service;

import com.disclose.paws.exception.ResourceNotFoundException;
import com.disclose.paws.model.UserEntity;
import com.disclose.paws.model.dto.UserDTO;
import com.disclose.paws.model.mapper.UserMapper;
import com.disclose.paws.repository.UserRepository;
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
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating new user: {}", userDTO.getName());
        if (userDTO.getName() == null || userDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }

        UserEntity userEntity = userMapper.toEntity(userDTO);
        UserEntity savedUser = userRepository.save(userEntity);
        log.info("User created with ID: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    public List<UserDTO> getAllUsers() {
        log.info("Retrieving all users");
        List<UserEntity> users = userRepository.findAll();
        log.info("Found {} users", users.size());
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        log.info("Finding user with ID: {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        log.info("User found: {}", userEntity.getName());
        return userMapper.toDto(userEntity);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        UserEntity userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        userRepository.delete(userToDelete);
        log.info("User successfully deleted");
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        log.info("Updating user with ID: {}", userDTO.getId());
        if (userDTO.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        UserEntity userToUpdate = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userDTO.getId()));

        UserEntity updatedEntity = userMapper.toEntity(userDTO);
        updatedEntity.setVersion(userToUpdate.getVersion());

        UserEntity savedEntity = userRepository.save(updatedEntity);
        log.info("User successfully updated: {}", savedEntity.getName());
        return userMapper.toDto(savedEntity);
    }

    public Page<UserDTO> findUsers(String name, String email, Boolean active, String role,
                                   String username, String phoneNumber, String city,
                                   String state, String country, String postalCode,
                                   Pageable pageable) {

        log.info("Searching users with filters: name={}, email={}, active={}, role={}, username={}, phoneNumber={}, city={}, state={}, country={}, postalCode={}",
                getValueOrDefault(name), getValueOrDefault(email), getValueOrDefault(active),
                getValueOrDefault(role), getValueOrDefault(username), getValueOrDefault(phoneNumber),
                getValueOrDefault(city), getValueOrDefault(state), getValueOrDefault(country),
                getValueOrDefault(postalCode));

        Page<UserEntity> userEntities = userRepository.findAll((root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            addStringLikeFilter(predicates, root, cb, "name", name);
            addStringLikeFilter(predicates, root, cb, "email", email);
            addStringLikeFilter(predicates, root, cb, "role", role);
            addStringLikeFilter(predicates, root, cb, "username", username);
            addStringLikeFilter(predicates, root, cb, "phoneNumber", phoneNumber);
            addStringLikeFilter(predicates, root, cb, "city", city);
            addStringLikeFilter(predicates, root, cb, "state", state);
            addStringLikeFilter(predicates, root, cb, "country", country);
            addStringLikeFilter(predicates, root, cb, "postalCode", postalCode);

            if (active != null) {
                predicates.add(cb.equal(root.get("active"), active));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable);

        log.info("Found {} users on the current page", userEntities.getNumberOfElements());
        return userEntities.map(userMapper::toDto);
    }

    private void addStringLikeFilter(List<jakarta.persistence.criteria.Predicate> predicates,
                                     jakarta.persistence.criteria.Root<UserEntity> root,
                                     jakarta.persistence.criteria.CriteriaBuilder cb,
                                     String field, String value) {
        if (value != null && !value.trim().isEmpty()) {
            String searchTerm = "%" + value.toLowerCase().trim() + "%";
            predicates.add(cb.like(cb.lower(root.get(field)), searchTerm));
        }
    }
}