package com.sjsu.cmpe272.tamales.tamalesHr.repository;

import com.sjsu.cmpe272.tamales.tamalesHr.model.DepartmentEmployee;
import com.sjsu.cmpe272.tamales.tamalesHr.model.DepartmentEmployeeId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface DepartmentEmployeeRepository extends JpaRepository<DepartmentEmployee, DepartmentEmployeeId> {

    @EntityGraph(attributePaths = "employee")
    @Query("SELECT de FROM DepartmentEmployee de WHERE de.id.emp_no = :emp_no ORDER BY de.to_date DESC")
    List<DepartmentEmployee> findByEmpNo(@Param("emp_no") Integer emp_no);

    @EntityGraph(attributePaths = "employee")
    @Query("SELECT de FROM DepartmentEmployee de WHERE de.id.dept_no = :dept_no")
    List<DepartmentEmployee> findActiveEmployees(@Param("dept_no") String dept_no);
}
