package com.sjsu.cmpe272.tamales.tamalesHr.repository;

import com.sjsu.cmpe272.tamales.tamalesHr.model.DeptManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentManagerRepository extends JpaRepository<DeptManager, Long> {
    @Query("SELECT dm FROM DeptManager dm WHERE dm.id.dept_no = :deptNo ORDER BY dm.to_date DESC")
    List<DeptManager> findDepartmentManager(@Param("deptNo") String deptNo);
}
