package com.sjsu.cmpe272.tamales.tamalesHr.repository;

import com.sjsu.cmpe272.tamales.tamalesHr.model.DepartmentEmployee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface DepartmentEmployeeRepository extends JpaRepository<DepartmentEmployee, Long> {

    @Query("SELECT de FROM DepartmentEmployee de WHERE de.id.emp_no = :emp_no")
    List<DepartmentEmployee> findByEmpNo(@Param("emp_no") Integer emp_no);
}
