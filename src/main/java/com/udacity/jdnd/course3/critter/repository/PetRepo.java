package com.udacity.jdnd.course3.critter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.udacity.jdnd.course3.critter.pet.Pet;

@Repository
public interface PetRepo extends JpaRepository<Pet, Long> {
	List<Pet> findByOwnerId(Long ownerId);

}
