package com.udacity.jdnd.course3.critter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepo;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.user.Employee;

@Transactional
@Service
public class ScheduleService {

	@Autowired
	private ScheduleRepo scheduleRepo;

	public Schedule saveSchedule(Schedule schedule) {
		return scheduleRepo.save(schedule);
	}

	public List<Schedule> getAllSchedules() {
		return scheduleRepo.findAll();
	}

	public Optional<Schedule> getSchedule(Long id) {
		return scheduleRepo.findById(id);
	}

	/**
	 * Get Schedules for a pet
	 * 
	 * @param petId
	 * @return
	 */
	public List<Schedule> getScheduleForPet(long petId) {

		List<Schedule> petScheduleList = new ArrayList<>();

		List<Schedule> allScheduleList = getAllSchedules();

		if (!CollectionUtils.isEmpty(allScheduleList)) {
			for (Schedule schedule : allScheduleList) {

				if (!CollectionUtils.isEmpty(schedule.getPets())) {
					for (Pet pet : schedule.getPets()) {

						if (petId == pet.getId()) {
							petScheduleList.add(schedule);
							break;
						}
					}
				}
			}
		}
		return petScheduleList;
	}

	/**
	 * Get Schedules for an employee
	 * 
	 * @param employeeId
	 * @return
	 */
	public List<Schedule> getScheduleForEmployee(long employeeId) {

		List<Schedule> empScheduleList = new ArrayList<>();

		List<Schedule> allScheduleList = getAllSchedules();

		if (!CollectionUtils.isEmpty(allScheduleList)) {
			for (Schedule schedule : allScheduleList) {

				if (!CollectionUtils.isEmpty(schedule.getEmployees())) {
					for (Employee emp : schedule.getEmployees()) {

						if (employeeId == emp.getId()) {
							empScheduleList.add(schedule);
							break;
						}
					}
				}
			}
		}
		return empScheduleList;
	}

	/**
	 * Get Schedules for customer
	 * 
	 * @param customerId
	 * @return
	 */
	public List<Schedule> getScheduleForCustomer(long customerId) {

		List<Schedule> customerScheduleList = new ArrayList<>();

		List<Schedule> allScheduleList = getAllSchedules();

		if (!CollectionUtils.isEmpty(allScheduleList)) {
			for (Schedule schedule : allScheduleList) {

				if (!CollectionUtils.isEmpty(schedule.getPets())) {

					List<Long> customerIds = schedule.getPets().stream().map(pet -> pet.getOwner().getId())
							.collect(Collectors.toList());
					if (customerIds.contains(customerId)) {
						customerScheduleList.add(schedule);
					}
				}
			}
		}
		return customerScheduleList;
	}

}
