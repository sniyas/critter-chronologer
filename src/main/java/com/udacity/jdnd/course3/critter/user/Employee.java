package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.views.Views;

@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@JsonView(Views.Public.class)
	@Nationalized
	private String name;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	private Set<EmployeeSkill> skills;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	private Set<DayOfWeek> daysAvailable;

	public Employee(String ename, Set<EmployeeSkill> eskills, Set<DayOfWeek> edaysAvailable) {

		this.name = ename;
		this.skills = eskills;
		this.daysAvailable = edaysAvailable;
	}

	public Employee() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<EmployeeSkill> getSkills() {
		return skills;
	}

	public void setSkills(Set<EmployeeSkill> skills) {
		this.skills = skills;
	}

	public Set<DayOfWeek> getDaysAvailable() {
		return daysAvailable;
	}

	public void setDaysAvailable(Set<DayOfWeek> daysAvailable) {
		this.daysAvailable = daysAvailable;
	}
}
