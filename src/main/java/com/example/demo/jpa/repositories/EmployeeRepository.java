package com.example.demo.jpa.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	Employee findByName(String name);

	Collection<Employee> findByNameAndSalary(String name, long salary);

	Collection<Employee> findByNameOrSalary(String name, long salary);

	@Query("Select e from Employee e where e.salary < :treshold")
	List<Employee> findAllWithLowSalary(@Param("treshold") long salary);

}
