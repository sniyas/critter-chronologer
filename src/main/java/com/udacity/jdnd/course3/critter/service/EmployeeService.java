package com.udacity.jdnd.course3.critter.service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.udacity.jdnd.course3.critter.repository.EmployeeRepo;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

@Transactional
@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepo employeeRepo;

	public Employee saveEmployee(Employee employee) {
		return employeeRepo.save(employee);
	}

	public List<Employee> getAllEmployees() {
		return employeeRepo.findAll();
	}

	public Optional<Employee> getEmployee(Long employeeId) {
		return employeeRepo.findById(employeeId);
	}

	public List<Employee> getAvailableEmployees(DayOfWeek dayOfWeek, Set<EmployeeSkill> eSkillSet) {

		List<Employee> availableEmployeeList = new ArrayList<>();

		List<Employee> employeeList = getAllEmployees();

		if (!CollectionUtils.isEmpty(employeeList)) {
			for (Employee employee : employeeList) {
				if (employee.getDaysAvailable().contains(dayOfWeek) && employee.getSkills().containsAll(eSkillSet)) {
					availableEmployeeList.add(employee);
				}
			}
		}
		return availableEmployeeList;
	}

}
