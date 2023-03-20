package com.udacity.jdnd.course3.critter.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.Employee;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private PetService petService;

	/**
	 * Creates a new schedule
	 * 
	 * @param scheduleDTO
	 * @return
	 * @throws Exception
	 */
	@PostMapping
	public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) throws Exception {

		List<Employee> employees = new ArrayList<>();
		if (scheduleDTO.getEmployeeIds() != null) {
			for (Long empId : scheduleDTO.getEmployeeIds()) {
				Employee employee = employeeService.getEmployee(empId)
						.orElseThrow(() -> new Exception("Employee not found for id " + empId));
				employees.add(employee);
			}
		}

		List<Pet> pets = new ArrayList<>();
		if (scheduleDTO.getPetIds() != null) {
			for (Long petId : scheduleDTO.getPetIds()) {
				Pet pet = petService.getPet(petId).orElseThrow(() -> new Exception("Pet not found for id " + petId));
				pets.add(pet);
			}
		}

		Schedule schedule = new Schedule(scheduleDTO.getDate(), scheduleDTO.getActivities(), employees, pets);

		schedule = scheduleService.saveSchedule(schedule);

		scheduleDTO.setId(schedule.getId());

		return scheduleDTO;
	}

	/**
	 * Fetches all schedules created
	 * 
	 * @return
	 */
	@GetMapping
	public List<ScheduleDTO> getAllSchedules() {

		List<Schedule> scheduleList = scheduleService.getAllSchedules();
		return convertScheduleListToDTOs(scheduleList);
	}

	/**
	 * Fetches Schedules for a pet
	 * 
	 * @param petId
	 * @return
	 */
	@GetMapping("/pet/{petId}")
	public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {

		List<Schedule> petScheduleList = scheduleService.getScheduleForPet(petId);
		return convertScheduleListToDTOs(petScheduleList);
	}

	/**
	 * Fetches schedules for an employee
	 * 
	 * @param employeeId
	 * @return
	 */
	@GetMapping("/employee/{employeeId}")
	public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {

		List<Schedule> empScheduleList = scheduleService.getScheduleForEmployee(employeeId);
		return convertScheduleListToDTOs(empScheduleList);
	}

	/**
	 * Fetches schedules for a customer
	 * 
	 * @param customerId
	 * @return
	 */
	@GetMapping("/customer/{customerId}")
	public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {

		List<Schedule> customerScheduleList = scheduleService.getScheduleForCustomer(customerId);
		return convertScheduleListToDTOs(customerScheduleList);
	}

	/**
	 * Method to convert entity to DTO
	 * 
	 * @param scheduleList
	 * @return
	 */
	private List<ScheduleDTO> convertScheduleListToDTOs(List<Schedule> scheduleList) {

		List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
		ScheduleDTO scheduleDTO = null;
		for (Schedule schedule : scheduleList) {

			List<Long> empIdList = new ArrayList<>();
			List<Long> petIdList = new ArrayList<>();
			if (!CollectionUtils.isEmpty(schedule.getEmployees())) {
				empIdList = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
			}
			if (!CollectionUtils.isEmpty(schedule.getPets())) {
				petIdList = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
			}
			scheduleDTO = new ScheduleDTO(schedule.getId(), empIdList, petIdList, schedule.getDate(),
					schedule.getActivities());
			scheduleDTOList.add(scheduleDTO);
		}
		return scheduleDTOList;
	}
}
