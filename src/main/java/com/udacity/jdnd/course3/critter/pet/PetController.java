package com.udacity.jdnd.course3.critter.pet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.user.Customer;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

	@Autowired
	private PetService petService;

	@Autowired
	private CustomerService customerService;

	/**
	 * Save a new pet
	 * 
	 * @param petDTO
	 * @return
	 * @throws Exception
	 */
	@PostMapping
	public PetDTO savePet(@RequestBody PetDTO petDTO) throws Exception {

		Customer owner = customerService.getCustomer(petDTO.getOwnerId())
				.orElseThrow(() -> new Exception("Owner not found for id " + petDTO.getOwnerId()));

		Pet pet = new Pet(petDTO.getName(), petDTO.getNotes(), owner, petDTO.getBirthDate(), petDTO.getType());
		pet = petService.savePet(pet, owner);

		petDTO.setId(pet.getId());

		return petDTO;
	}

	/**
	 * Fetches a pet
	 * 
	 * @param petId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/{petId}")
	public PetDTO getPet(@PathVariable long petId) throws Exception {

		Pet pet = petService.getPet(petId).orElseThrow(() -> new Exception("Pet not found for id " + petId));

		PetDTO petDTO = new PetDTO(pet.getId(), pet.getBirthDate(), pet.getName(), pet.getNotes(),
				pet.getOwner().getId(), pet.getType());
		return petDTO;

	}

	/**
	 * Fetches all pets
	 * 
	 * @return
	 */
	@GetMapping
	public List<PetDTO> getPets() {

		List<Pet> petList = petService.getAllPets();

		List<PetDTO> petDTOList = new ArrayList<>();
		PetDTO petDTO = null;
		if (!CollectionUtils.isEmpty(petList)) {
			for (Pet pet : petList) {
				petDTO = new PetDTO(pet.getId(), pet.getBirthDate(), pet.getName(), pet.getNotes(),
						pet.getOwner().getId(), pet.getType());
				petDTOList.add(petDTO);
			}
		}

		return petDTOList;

	}

	/**
	 * Fetches pets for a customer
	 * 
	 * @param ownerId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/owner/{ownerId}")
	public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) throws Exception {

		List<Pet> pets = petService.getAllPetsByCustomer(ownerId);
		List<PetDTO> petDTOs = new ArrayList<>();
		PetDTO petDTO = null;

		if (!CollectionUtils.isEmpty(pets)) {
			for (Pet pet : pets) {

				petDTO = new PetDTO(pet.getId(), pet.getBirthDate(), pet.getName(), pet.getNotes(),
						pet.getOwner().getId(), pet.getType());
				petDTOs.add(petDTO);
			}
		}

		return petDTOs;
	}
}
