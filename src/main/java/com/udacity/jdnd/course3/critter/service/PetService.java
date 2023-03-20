package com.udacity.jdnd.course3.critter.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepo;
import com.udacity.jdnd.course3.critter.repository.PetRepo;
import com.udacity.jdnd.course3.critter.user.Customer;

@Transactional
@Service
public class PetService {

	@Autowired
	private PetRepo petRepo;
	
	@Autowired
	private CustomerRepo customerRepo;

	public Pet savePet(Pet pet, Customer owner) {
		pet = petRepo.save(pet);
		owner.getPets().add(pet);
		customerRepo.save(owner);
		return pet;
	}

	public List<Pet> getAllPets() {
		return petRepo.findAll();
	}

	public Optional<Pet> getPet(long petId) {
		return petRepo.findById(petId);
	}
	
	public List<Pet> getAllPetsByCustomer(long customerId) {
		return petRepo.findByOwnerId(customerId);
	}

}
