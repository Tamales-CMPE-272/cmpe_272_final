package com.sjsu.cmpe272.tamales.tamalesHr.controller;

import com.sjsu.cmpe272.tamales.tamalesHr.model.Employee;
import com.sjsu.cmpe272.tamales.tamalesHr.model.Salary;
import com.sjsu.cmpe272.tamales.tamalesHr.model.SalaryDTO;
import com.sjsu.cmpe272.tamales.tamalesHr.repository.EmployeeRepository;
import com.sjsu.cmpe272.tamales.tamalesHr.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/tamalesHr")
public class MainController {

  private final EmployeeRepository employeeRepository;
  private final SalaryRepository salaryRepository;

  public MainController(EmployeeRepository employeeRepository, SalaryRepository salaryRepository) {
    this.employeeRepository = employeeRepository;
    this.salaryRepository = salaryRepository;
  }

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
