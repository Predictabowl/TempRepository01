package com.example.demo.model.dto;

import java.util.Objects;

public class EmployeeDTO {
	
	private Long id;
	private String name;
	private long salary;
	
	
	public EmployeeDTO() {
	}


	public EmployeeDTO(Long id, String name, long salary) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public long getSalary() {
		return salary;
	}


	public void setSalary(long salary) {
		this.salary = salary;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id, name, salary);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeDTO other = (EmployeeDTO) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name) && salary == other.salary;
	}


	@Override
	public String toString() {
		return "EmployeeDTO [id=" + id + ", name=" + name + ", salary=" + salary + "]";
	}
	
	
}
