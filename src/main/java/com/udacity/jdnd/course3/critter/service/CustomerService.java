package com.udacity.jdnd.course3.critter.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.repository.CustomerRepo;
import com.udacity.jdnd.course3.critter.user.Customer;

@Transactional
@Service
public class CustomerService {

	@Autowired
	private CustomerRepo customerRepo;

	public Customer saveCustomer(Customer customer) {
		return customerRepo.save(customer);
	}

	public List<Customer> getAllCustomers() {

		return customerRepo.findAll();
	}

	public Optional<Customer> getCustomer(Long customerId) {
		return customerRepo.findById(customerId);
	}

}
