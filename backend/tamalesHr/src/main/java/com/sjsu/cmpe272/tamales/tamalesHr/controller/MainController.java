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
  @Autowired private SalaryRepository salaryRepository;

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
      List<String> titleNames = titles.stream()
                                      .map(Title::getTitle)
                                      .distinct()
                                      .toList();

      List<DepartmentEmployee> deptEmps = deptEmpRepository.findByEmpNo(id.intValue());
      List<String> deptNames = deptEmps.stream()
                                      .map(DepartmentEmployee::getDept_no)
                                      .map(deptNo -> departmentRepository.findById(deptNo))
                                      .filter(Optional::isPresent)
                                      .map(Optional::get)
                                      .map(Department::getDept_name)
                                      .distinct()
                                      .toList();

      Profile profile = new Profile(
          employee.getEmp_no(),
          employee.getFirst_name(),
          employee.getLast_name(),
          employee.getGender(),
          employee.getBirth_date(),
          employee.getHire_date(),
          titleNames,
          deptNames
      );

      return new ResponseEntity<>(profile, HttpStatus.OK);
  }

  @GetMapping(path = "/salary/{employee_id}")
  public @ResponseBody ResponseEntity<List<SalaryDTO>> getSalaryByEmployeeId(@PathVariable Long employee_id) {
    List<Salary> salaries = salaryRepository.findByIdEmpNo(employee_id);

    if (salaries.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    List<SalaryDTO> dtoList = salaries.stream()
            .map(s -> new SalaryDTO(
                    s.getId().getEmpNo(),
                    s.getId().getFrom_date(),
                    s.getTo_date(),
                    s.getSalary()
            ))
            .toList();

    return ResponseEntity.ok(dtoList);
  }
}
