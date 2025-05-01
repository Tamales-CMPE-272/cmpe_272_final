package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DepartmentEmployeeId implements Serializable {
    private Integer emp_no;
    private String dept_no;

    public DepartmentEmployeeId() {}

    public DepartmentEmployeeId(Integer emp_no, String dept_no) {
        this.emp_no = emp_no;
        this.dept_no = dept_no;
    }

    public Integer getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(Integer emp_no) {
        this.emp_no = emp_no;
    }

    public String getDept_no() {
        return dept_no;
    }

    public void setDept_no(String dept_no) {
        this.dept_no = dept_no;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentEmployeeId that)) return false;
        return Objects.equals(emp_no, that.emp_no) && Objects.equals(dept_no, that.dept_no);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emp_no, dept_no);
    }
}
