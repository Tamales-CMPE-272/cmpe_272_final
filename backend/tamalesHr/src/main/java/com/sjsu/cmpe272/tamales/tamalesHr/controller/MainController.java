package com.sjsu.cmpe272.tamales.tamalesHr.controller;

import com.sjsu.cmpe272.tamales.tamalesHr.model.Employee;
import com.sjsu.cmpe272.tamales.tamalesHr.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/tamalesHr")
public class MainController {

  @Autowired private EmployeeRepository employeeRepository;

  @GetMapping(path = "/employees")
  public @ResponseBody Iterable<Employee> getAllUsers() {
    return employeeRepository.findAll();
  }
}
