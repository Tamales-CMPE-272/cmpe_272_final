package com.sjsu.cmpe272.tamales.tamalesHr;

import com.sjsu.cmpe272.tamales.tamalesHr.controller.MainController;
import com.sjsu.cmpe272.tamales.tamalesHr.model.*;
import com.sjsu.cmpe272.tamales.tamalesHr.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
class MainControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private EmployeeRepository employeeRepository;
  @MockBean private TitleRepository titleRepository;
  @MockBean private DepartmentEmployeeRepository deptEmpRepository;
  @MockBean private DepartmentRepository departmentRepository;
  @MockBean private SalaryRepository salaryRepository;
  @MockBean private DepartmentManagerRepository departmentManagerRepository;

  @Test
  @WithMockUser
  void testGetAllEmployees() throws Exception {
    Employee employee = new Employee();
    employee.setEmp_no(1);
    employee.setFirst_name("John");
    employee.setLast_name("Doe");

    Mockito.when(employeeRepository.findAll()).thenReturn(List.of(employee));

    mockMvc
        .perform(get("/tamalesHr/employees"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].emp_no").value(1))
        .andExpect(jsonPath("$[0].first_name").value("John"))
        .andExpect(jsonPath("$[0].last_name").value("Doe"));
  }

  @Test
  @WithMockUser
  void testGetEmployeeById_Found() throws Exception {
    Employee e = new Employee();
    e.setEmp_no(1);
    e.setFirst_name("John");
    e.setLast_name("Doe");

    Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(e));

    mockMvc
        .perform(get("/tamalesHr/employees/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.emp_no").value(1));
  }

  @Test
  @WithMockUser
  void testGetEmployeeById_NotFound() throws Exception {
    Mockito.when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

    mockMvc.perform(get("/tamalesHr/employees/999")).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  void testGetProfile_Found() throws Exception {
    Employee e = new Employee();
    e.setEmp_no(1);
    e.setFirst_name("John");
    e.setLast_name("Doe");

    Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(e));
    Mockito.when(titleRepository.findByEmpNoOrderByToDateDesc(anyInt()))
        .thenReturn(
            List.of(new Title(new TitleId(1, "Software Engineer", new Date()), new Date())));
    Mockito.when(deptEmpRepository.findByEmpNo(anyInt()))
        .thenReturn(
            List.of(
                new DepartmentEmployee(
                    new DepartmentEmployeeId(), new Date(), new Date(), new Employee())));

	Mockito.when(departmentRepository.findById("d001"))
        .thenReturn(Optional.of(new Department("d001", "Engineering")));

    mockMvc
        .perform(get("/tamalesHr/profile/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.first_name").value("John"));
  }

  @Test
  @WithMockUser
  void testGetProfile_NotFound() throws Exception {
    Mockito.when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

    mockMvc.perform(get("/tamalesHr/profile/999")).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  void testGetSalaryByEmployeeId_Found() throws Exception {
    SalaryId salaryId = new SalaryId(1, LocalDate.parse("2023-01-01"));
    Salary salary = new Salary();
    salary.setId(salaryId);
    salary.setTo_date(LocalDate.parse("2024-01-01"));
    salary.setSalary(50000);

    Mockito.when(salaryRepository.findByIdEmpNo(1L)).thenReturn(List.of(salary));

    mockMvc
        .perform(get("/tamalesHr/salary/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].salary").value(50000));
  }

  @Test
  @WithMockUser
  void testGetSalaryByEmployeeId_NotFound() throws Exception {
    Mockito.when(salaryRepository.findByIdEmpNo(anyLong())).thenReturn(List.of());

    mockMvc.perform(get("/tamalesHr/salary/999")).andExpect(status().isNotFound());
  }

	@Test
	@WithMockUser(roles = "Manager")
	void testGetDepartmentById_Found() throws Exception {
		Department department = new Department("d001", "Engineering");
		DepartmentEmployee activeEmp = new DepartmentEmployee();
		activeEmp.setEmp_no(1);
		activeEmp.setDept_no("d001");
		activeEmp.setTo_date(new Date(Long.MAX_VALUE));

		DeptManager manager = new DeptManager();
		manager.setEmp_no(2);
		manager.setDept_no("d001");
		manager.setTo_date(new Date(Long.MAX_VALUE));

		Mockito.when(departmentRepository.findById("d001")).thenReturn(Optional.of(department));
		Mockito.when(deptEmpRepository.findActiveEmployees("d001")).thenReturn(List.of(activeEmp));
		Mockito.when(departmentManagerRepository.findDepartmentManager("d001")).thenReturn(List.of(manager));

		mockMvc.perform(get("/tamalesHr/department/d001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.department.dept_name").value("Engineering"));
	}

	@Test
	@WithMockUser()
	void testGetDepartmentById_NotFound() throws Exception {

		Mockito.when(departmentRepository.findById("d999")).thenReturn(Optional.empty());

		mockMvc.perform(get("/tamalesHr/department/d999"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetDepartmentById_Unauthorized() throws Exception {
		Department department = new Department("d001", "Engineering");
		DepartmentEmployee activeEmp = new DepartmentEmployee();
		activeEmp.setEmp_no(1);
		activeEmp.setDept_no("d001");
		activeEmp.setTo_date(new Date(Long.MAX_VALUE));

		DeptManager manager = new DeptManager();
		manager.setEmp_no(2);
		manager.setDept_no("d001");
		manager.setTo_date(new Date(Long.MAX_VALUE));

		Mockito.when(departmentRepository.findById("d001")).thenReturn(Optional.of(department));
		Mockito.when(deptEmpRepository.findActiveEmployees("d001")).thenReturn(List.of(activeEmp));
		Mockito.when(departmentManagerRepository.findDepartmentManager("d001")).thenReturn(List.of(manager));

		mockMvc.perform(get("/tamalesHr/department/d001"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "Employee")
	void testGetDepartmentById_Forbidden() throws Exception {
		Department department = new Department("d001", "Engineering");
		DepartmentEmployee activeEmp = new DepartmentEmployee();
		activeEmp.setEmp_no(1);
		activeEmp.setDept_no("d001");
		activeEmp.setTo_date(new Date(Long.MAX_VALUE));

		DeptManager manager = new DeptManager();
		manager.setEmp_no(2);
		manager.setDept_no("d001");
		manager.setTo_date(new Date(Long.MAX_VALUE));

		Mockito.when(departmentRepository.findById("d001")).thenReturn(Optional.of(department));
		Mockito.when(deptEmpRepository.findActiveEmployees("d001")).thenReturn(List.of(activeEmp));
		Mockito.when(departmentManagerRepository.findDepartmentManager("d001")).thenReturn(List.of(manager));

		mockMvc.perform(put("/tamalesHr/department/d001/1005"))
				.andExpect(status().isForbidden());
	}

	@Test
	void testRemoveEmployeeDepartment_Success() throws Exception {
		Department department = new Department("d001", "Engineering");
		DepartmentEmployee activeEmp = new DepartmentEmployee();
		activeEmp.setEmp_no(1);
		activeEmp.setDept_no("d001");
		activeEmp.setTo_date(new Date(Long.MAX_VALUE));

		DeptManager manager = new DeptManager();
		manager.setEmp_no(2);
		manager.setDept_no("d001");
		manager.setTo_date(new Date(Long.MAX_VALUE));

		Mockito.when(departmentRepository.findById("d001")).thenReturn(Optional.of(department));
		Mockito.when(deptEmpRepository.findActiveEmployees("d001")).thenReturn(List.of(activeEmp));
		Mockito.when(departmentManagerRepository.findDepartmentManager("d001")).thenReturn(List.of(manager));

		mockMvc.perform(get("/tamalesHr/department/d001")
						.with(jwt().jwt(jwt -> jwt.claim("resource_access", Map.of(
								"tamalesHr-rest-api", Map.of("roles", List.of("Manager"))
						)))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.department.dept_name").value("Engineering"));
	}

	@Test
	void testRemoveEmployeeDepartment_EmployeeNotFound() throws Exception {
		Mockito.when(employeeRepository.existsById(1L)).thenReturn(false);

		mockMvc.perform(put("/tamalesHr/department/remove/1/d001")
						.with(jwt().jwt(jwt -> jwt.claim("resource_access", Map.of(
								"tamalesHr-rest-api", Map.of("roles", List.of("Manager"))
						)))))
				.andExpect(status().isNotFound());
	}

	@Test
	void testRemoveEmployeeDepartment_NoCurrentAssignment() throws Exception {
		Mockito.when(employeeRepository.existsById(1L)).thenReturn(true);
		Mockito.when(departmentRepository.existsById("d001")).thenReturn(true);
		Mockito.when(deptEmpRepository.findByEmpNo(1)).thenReturn(List.of());

		mockMvc.perform(put("/tamalesHr/department/remove/1/d001")
						.with(jwt().jwt(jwt -> jwt.claim("resource_access", Map.of(
								"tamalesHr-rest-api", Map.of("roles", List.of("Manager"))
						)))))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testUpdateEmployeeDepartment_Success() throws Exception {
		Integer empNo = 1;
		String deptNo = "d001";

		Employee employee = new Employee();
		employee.setEmp_no(empNo);

		Department department = new Department(deptNo, "Engineering");

		DepartmentEmployee existingAssignment = new DepartmentEmployee();
		existingAssignment.setEmp_no(empNo);
		existingAssignment.setDept_no("oldDept");
		existingAssignment.setTo_date(new Date(Long.MAX_VALUE));

		Mockito.when(employeeRepository.existsById(empNo.longValue())).thenReturn(true);
		Mockito.when(departmentRepository.existsById(deptNo)).thenReturn(true);
		Mockito.when(deptEmpRepository.findByEmpNo(empNo)).thenReturn(List.of(existingAssignment));
		Mockito.when(employeeRepository.getReferenceById(empNo.longValue())).thenReturn(employee);
		Mockito.when(deptEmpRepository.findActiveEmployees(deptNo)).thenReturn(List.of());
		Mockito.when(departmentManagerRepository.findDepartmentManager(deptNo)).thenReturn(List.of());
		Mockito.when(departmentRepository.findById(deptNo)).thenReturn(Optional.of(department));

		mockMvc.perform(put("/tamalesHr/department/update/{emp_no}/{dept_no}", empNo, deptNo)
						.with(jwt().jwt(jwt -> jwt.claim("resource_access", Map.of(
								"tamalesHr-rest-api", Map.of("roles", List.of("Manager"))
						)))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.department.dept_name").value("Engineering"));
	}

	@Test
	void testUpdateEmployeeDepartment_EmployeeNotFound() throws Exception {
		Mockito.when(employeeRepository.existsById(1L)).thenReturn(false);

		mockMvc.perform(put("/tamalesHr/department/update/1/d001")
						.with(jwt().jwt(jwt -> jwt.claim("resource_access", Map.of(
								"tamalesHr-rest-api", Map.of("roles", List.of("Manager"))
						)))))
				.andExpect(status().isNotFound());
	}
}
