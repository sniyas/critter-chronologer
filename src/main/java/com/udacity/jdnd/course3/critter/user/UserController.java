package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into
 * separate user and customer controllers would be fine too, though that is not
 * part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private PetService petService;

	@Autowired
	private EmployeeService employeeService;

	/**
	 * Save a new customer
	 * 
	 * @param customerDTO
	 * @return
	 */
	@PostMapping("/customer")
	public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {

		Customer customer = new Customer();
		customer.setName(customerDTO.getName());
		customer.setPhoneNumber(customerDTO.getPhoneNumber());

		customer = customerService.saveCustomer(customer);
		customerDTO.setId(customer.getId());

		return customerDTO;
	}

	/**
	 * Fetches all customers
	 * 
	 * @return
	 */
	@GetMapping("/customer")
	public List<CustomerDTO> getAllCustomers() {

		List<Customer> customerList = customerService.getAllCustomers();

		List<CustomerDTO> customerDTOList = new ArrayList<>();
		CustomerDTO customerDTO = null;

		for (Customer customer : customerList) {

			List<Long> petIds = null;

			if (!CollectionUtils.isEmpty(customer.getPets())) {
				petIds = new ArrayList<>();
				for (Pet pet : customer.getPets()) {
					petIds.add(pet.getId());
				}
			}
			customerDTO = new CustomerDTO(customer.getId(), customer.getName(), customer.getNotes(),
					customer.getPhoneNumber(), petIds);
			customerDTOList.add(customerDTO);
		}
		return customerDTOList;
	}

	/**
	 * Fetches owner for a pet
	 * 
	 * @param petId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/customer/pet/{petId}")
	public CustomerDTO getOwnerByPet(@PathVariable long petId) throws Exception {

		CustomerDTO customerDTO = null;
		
		Pet pet = petService.getPet(petId).orElseThrow(() -> new Exception("Pet not found for id " + petId));
		if (pet.getOwner() != null) {

			List<Long> petIds = null;
			if (!CollectionUtils.isEmpty(pet.getOwner().getPets())) {
				
				petIds = pet.getOwner().getPets().stream().map(Pet::getId).collect(Collectors.toList());
			}
			customerDTO = new CustomerDTO(pet.getOwner().getId(), pet.getOwner().getName(), pet.getOwner().getNotes(),
					pet.getOwner().getPhoneNumber(), petIds);
		}
		
		return customerDTO;
	}

	/**
	 * Save a new employee
	 * 
	 * @param employeeDTO
	 * @return
	 */
	@PostMapping("/employee")
	public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {

		Employee employee = new Employee(employeeDTO.getName(), employeeDTO.getSkills(),
				employeeDTO.getDaysAvailable());
		employee = employeeService.saveEmployee(employee);
		employeeDTO.setId(employee.getId());
		return employeeDTO;
	}

	/**
	 * Fetch an employee
	 * 
	 * @param employeeId
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/employee/{employeeId}")
	public EmployeeDTO getEmployee(@PathVariable long employeeId) throws Exception {
		Employee employee = employeeService.getEmployee(employeeId)
				.orElseThrow(() -> new Exception("Employee not found for id " + employeeId));
		EmployeeDTO employeeDTO = new EmployeeDTO(employee.getId(), employee.getName(), employee.getDaysAvailable(),
				employee.getSkills());
		return employeeDTO;
	}

	/**
	 * Set available days for an employee
	 * 
	 * @param daysAvailable
	 * @param employeeId
	 * @throws Exception
	 */
	@PutMapping("/employee/{employeeId}")
	public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId)
			throws Exception {

		Employee employee = employeeService.getEmployee(employeeId)
				.orElseThrow(() -> new Exception("Employee not found for id " + employeeId));
		employee.setDaysAvailable(daysAvailable);
		employeeService.saveEmployee(employee);
	}

	/**
	 * Find available employees for date and skill
	 * 
	 * @param employeeRequestDTO
	 * @return
	 */
	@GetMapping("/employee/availability")
	public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) {

		List<Employee> availableEmployeeList = employeeService
				.getAvailableEmployees(employeeRequestDTO.getDate().getDayOfWeek(), employeeRequestDTO.getSkills());

		List<EmployeeDTO> availableEmployeeDTOs = new ArrayList<>();
		EmployeeDTO employeeDTO = null;
		if (!CollectionUtils.isEmpty(availableEmployeeList)) {
			for (Employee employee : availableEmployeeList) {
				employeeDTO = new EmployeeDTO(employee.getId(), employee.getName(), employee.getDaysAvailable(),
						employee.getSkills());
				availableEmployeeDTOs.add(employeeDTO);
			}
		}

		return availableEmployeeDTOs;
	}

}
