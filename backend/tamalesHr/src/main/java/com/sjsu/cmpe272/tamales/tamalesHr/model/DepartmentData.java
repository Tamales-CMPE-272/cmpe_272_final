package com.sjsu.cmpe272.tamales.tamalesHr.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DepartmentData {
    Department department;
    List<DepartmentEmployee> departmentEmployee;
    List<DeptManager> deptManager;

    public DepartmentData() {
    }

    public DepartmentData(Department department, List<DepartmentEmployee> departmentEmployee, List<DeptManager> deptManager) {
        this.department = department;
        this.departmentEmployee = departmentEmployee;
        this.deptManager = deptManager;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setDepartmentEmployee(List<DepartmentEmployee> departmentEmployee) {
        this.departmentEmployee = departmentEmployee;
    }

    public void setDeptManager(List<DeptManager> deptManager) {
        this.deptManager = deptManager;
    }
}
