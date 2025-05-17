package com.disclose.paws.repository;

import com.disclose.paws.model.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long>, JpaSpecificationExecutor<PetEntity> {

}
