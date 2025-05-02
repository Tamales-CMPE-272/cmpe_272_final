package com.sjsu.cmpe272.tamales.tamalesHr.controller;

import com.sjsu.cmpe272.tamales.tamalesHr.model.*;
import com.sjsu.cmpe272.tamales.tamalesHr.dto.Profile;
import com.sjsu.cmpe272.tamales.tamalesHr.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping(path = "/tamalesHr")
public class MainController {

  @Autowired private EmployeeRepository employeeRepository;
  @Autowired private TitleRepository titleRepository;
  @Autowired private DepartmentEmployeeRepository deptEmpRepository;
  @Autowired private DepartmentRepository departmentRepository;

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

  @GetMapping("/profile/{id}")
  public ResponseEntity<Profile> getProfile(@PathVariable Long id) {
    Optional<Employee> empOpt = employeeRepository.findById(id);
    if (empOpt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    Employee employee = empOpt.get();

    List<Title> titles = titleRepository.findByEmpNoOrderByToDateDesc(id.intValue());
    String title = titles.isEmpty() ? null : titles.get(0).getTitle();

    String deptName = deptEmpRepository.findByEmpNo(id.intValue())
      .stream()
      .findFirst()
      .map(deptEmp -> deptEmp.getDept_no())
      .flatMap(deptNo -> departmentRepository.findById(deptNo))
      .map(Department::getDept_name)
      .orElse(null);

    Profile profile = new Profile(
        employee.getEmp_no(),
        employee.getFirst_name(),
        employee.getLast_name(),
        employee.getGender(),
        employee.getBirth_date(),
        employee.getHire_date(),
        title,
        deptName
    );

    return new ResponseEntity<>(profile, HttpStatus.OK);
  }

}
