package com.sjsu.cmpe272.tamales.tamalesHr.model;

import java.io.Serializable;
import java.util.Objects;

public class DeptManagerId implements Serializable {
    private Integer emp_no;
    private String dept_no;

    public DeptManagerId() {}

    public DeptManagerId(Integer emp_no, String dept_no) {
        this.emp_no = emp_no;
        this.dept_no = dept_no;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeptManagerId)) return false;
        DeptManagerId that = (DeptManagerId) o;
        return Objects.equals(emp_no, that.emp_no) &&
                Objects.equals(dept_no, that.dept_no);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emp_no, dept_no);
    }
}
