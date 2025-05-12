package com.sjsu.cmpe272.tamales.tamalesHr.controller;

import com.sjsu.cmpe272.tamales.tamalesHr.model.*;
import com.sjsu.cmpe272.tamales.tamalesHr.dto.Profile;
import com.sjsu.cmpe272.tamales.tamalesHr.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping(path = "/tamalesHr")
public class MainController {

  @Autowired private EmployeeRepository employeeRepository;
  @Autowired private TitleRepository titleRepository;
  @Autowired private DepartmentEmployeeRepository deptEmpRepository;
  @Autowired private DepartmentRepository departmentRepository;
  @Autowired private SalaryRepository salaryRepository;
  @Autowired private DepartmentManagerRepository departmentManagerRepository;

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
    List<String> titleNames = titles.stream().map(Title::getTitle).distinct().toList();

    List<DepartmentEmployee> deptEmps = deptEmpRepository.findByEmpNo(id.intValue());
    List<String> deptNames =
        deptEmps.stream()
            .map(DepartmentEmployee::getDept_no)
            .map(deptNo -> departmentRepository.findById(deptNo))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(Department::getDept_name)
            .distinct()
            .toList();

    Profile profile =
        new Profile(
            employee.getEmp_no(),
            employee.getFirst_name(),
            employee.getLast_name(),
            employee.getGender(),
            employee.getBirth_date(),
            employee.getHire_date(),
            titleNames,
            deptNames);

    return new ResponseEntity<>(profile, HttpStatus.OK);
  }

  @GetMapping("/department/{dept_no}")
  @PreAuthorize("hasRole('Manager')")
  public ResponseEntity<DepartmentData> getDepartmentById(@PathVariable String dept_no) {
    List<DepartmentEmployee> activeEmployees =
        deptEmpRepository.findActiveEmployees(dept_no).stream()
            .filter(DepartmentEmployee::isCurrentlyEnrolled)
            .toList();
    Optional<Department> departmentOptional = departmentRepository.findById(dept_no);
    List<DeptManager> manager =
            departmentManagerRepository.findDepartmentManager(dept_no).stream()
                    .filter(DeptManager::isCurrentlyEnrolled)
                    .toList();

    DepartmentData departmentData;

    if (departmentOptional.isPresent()) {
      departmentData = new DepartmentData(departmentOptional.get(), activeEmployees, manager);
      return ResponseEntity.ok(departmentData);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @PutMapping("/department/remove/{emp_no}/{dept_no}")
  @PreAuthorize("hasRole('Manager')")
  public ResponseEntity<DepartmentData> removeEmployeeDepartment(
      @PathVariable Integer emp_no, @PathVariable String dept_no) {
    if (!employeeRepository.existsById(emp_no.longValue())) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    if (!departmentRepository.existsById(dept_no)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    List<DepartmentEmployee> assignments = deptEmpRepository.findByEmpNo(emp_no);
    DepartmentEmployee current =
        assignments.stream()
            .filter(de -> de.getTo_date().toString().startsWith("9999"))
            .findFirst()
            .orElse(null);
    if (current == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    Date today = new Date();
    current.setTo_date(today);
    deptEmpRepository.save(current);

    List<DepartmentEmployee> activeEmployees =
        deptEmpRepository.findActiveEmployees(dept_no).stream()
            .filter(DepartmentEmployee::isCurrentlyEnrolled)
            .toList();
    Optional<Department> departmentOptional = departmentRepository.findById(dept_no);
    List<DeptManager> manager =
        departmentManagerRepository.findDepartmentManager(dept_no).stream()
            .filter(DeptManager::isCurrentlyEnrolled)
            .toList();

    DepartmentData departmentData;

    if (departmentOptional.isPresent()) {
      departmentData = new DepartmentData(departmentOptional.get(), activeEmployees, manager);
      return ResponseEntity.ok(departmentData);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @PutMapping("/department/update/{emp_no}/{dept_no}")
  @Transactional
  @PreAuthorize("hasRole('Manager')")
  public ResponseEntity<DepartmentData> updateEmployeeDepartment(
      @PathVariable Integer emp_no, @PathVariable String dept_no) {
    if (!employeeRepository.existsById(emp_no.longValue())) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    if (!departmentRepository.existsById(dept_no)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    List<DepartmentEmployee> assignments = deptEmpRepository.findByEmpNo(emp_no);
    DepartmentEmployee current =
        assignments.stream()
            .filter(de -> de.getTo_date().toString().startsWith("9999"))
            .findFirst()
            .orElse(null);
    Date today = new Date();
    if (current != null) {
      // remove employee from the other department if already there
      current.setTo_date(today);
      deptEmpRepository.save(current);
    }

    DepartmentEmployeeId id = new DepartmentEmployeeId(emp_no, dept_no);
    DepartmentEmployee newAssignment = new DepartmentEmployee();
    Employee employee = employeeRepository.getReferenceById(emp_no.longValue());
    newAssignment.setId(id);
    newAssignment.setEmp_no(emp_no);
    newAssignment.setDept_no(dept_no);
    newAssignment.setFrom_date(today);
    newAssignment.setEmployee(employee);
    Calendar cal = Calendar.getInstance();
    cal.set(9999, Calendar.JANUARY, 1, 0, 0, 0);
    cal.set(Calendar.MILLISECOND, 0);
    newAssignment.setTo_date(cal.getTime());

    deptEmpRepository.save(newAssignment);

    deptEmpRepository.flush();

    List<DepartmentEmployee> activeEmployees =
        deptEmpRepository.findActiveEmployees(dept_no).stream()
            .filter(DepartmentEmployee::isCurrentlyEnrolled)
            .toList();
    Optional<Department> departmentOptional = departmentRepository.findById(dept_no);
    List<DeptManager> manager =
        departmentManagerRepository.findDepartmentManager(dept_no).stream()
            .filter(DeptManager::isCurrentlyEnrolled)
            .toList();

    DepartmentData departmentData;

    if (departmentOptional.isPresent()) {
      departmentData = new DepartmentData(departmentOptional.get(), activeEmployees, manager);
      return ResponseEntity.ok(departmentData);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @GetMapping(path = "/salary/{employee_id}")
  public @ResponseBody ResponseEntity<List<SalaryDTO>> getSalaryByEmployeeId(
      @PathVariable Long employee_id) {
    List<Salary> salaries = salaryRepository.findByIdEmpNo(employee_id);

    if (salaries.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    List<SalaryDTO> dtoList =
        salaries.stream()
            .map(
                s ->
                    new SalaryDTO(
                        s.getId().getEmpNo(),
                        s.getId().getFrom_date(),
                        s.getTo_date(),
                        s.getSalary()))
            .toList();

    return ResponseEntity.ok(dtoList);
  }
}
