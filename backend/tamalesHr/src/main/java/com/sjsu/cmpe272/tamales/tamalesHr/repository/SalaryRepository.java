package com.sjsu.cmpe272.tamales.tamalesHr.repository;

import com.sjsu.cmpe272.tamales.tamalesHr.model.Salary;
import com.sjsu.cmpe272.tamales.tamalesHr.model.SalaryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryRepository extends JpaRepository<Salary, SalaryId> {
    List<Salary> findByIdEmpNo(Long emp_no);
}