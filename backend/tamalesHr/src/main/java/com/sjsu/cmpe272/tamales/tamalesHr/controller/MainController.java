package com.sjsu.cmpe272.tamales.tamalesHr.controller;

import com.sjsu.cmpe272.tamales.tamalesHr.model.Employee;
import com.sjsu.cmpe272.tamales.tamalesHr.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping(path = "/tamalesHr")
public class MainController {

  @Autowired private EmployeeRepository employeeRepository;

  @GetMapping(path = "/employees")
  public @ResponseBody Iterable<Employee> getAllUsers() {
    return employeeRepository.findAll();
  }

  @GetMapping(path = "/employees/{id}")
  public @ResponseBody ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    Optional<Employee> employee = employeeRepository.findById(id);
    if (employee.isPresent()) {
      return new ResponseEntity<>(employee.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
