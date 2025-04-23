package com.sjsu.cmpe272.tamales.tamalesHr.repository;

import com.sjsu.cmpe272.tamales.tamalesHr.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, String> {}

