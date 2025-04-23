package com.sjsu.cmpe272.tamales.tamalesHr.controller;

import com.sjsu.cmpe272.tamales.tamalesHr.model.Employee;
import com.sjsu.cmpe272.tamales.tamalesHr.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

  @Autowired
  private EmployeeRepository repo;

  @GetMapping
  public List<Employee> getAll() {
    return repo.findAll();
  }

  @PostMapping
  public Employee create(@RequestBody Employee emp) {
    return repo.save(emp);
  }

  @PutMapping("/{id}")
  public Employee update(@PathVariable int id, @RequestBody Employee emp) {
    emp.setEmpNo(id);
    return repo.save(emp);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable int id) {
    repo.deleteById(id);
  }
}

