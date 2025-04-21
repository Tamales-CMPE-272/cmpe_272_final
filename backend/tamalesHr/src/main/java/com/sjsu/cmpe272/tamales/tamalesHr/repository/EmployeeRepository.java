package com.sjsu.cmpe272.tamales.tamalesHr.repository;

import com.sjsu.cmpe272.tamales.tamalesHr.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


}
