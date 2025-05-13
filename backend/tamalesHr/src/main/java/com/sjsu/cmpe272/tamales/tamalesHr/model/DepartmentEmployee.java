package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "dept_emp")
public class DepartmentEmployee {

    @EmbeddedId
    private DepartmentEmployeeId id;

    @Temporal(TemporalType.DATE)
    private Date from_date;

    @Temporal(TemporalType.DATE)
    private Date to_date;

    @ManyToOne
    @JoinColumn(
            name = "emp_no",
            referencedColumnName = "emp_no",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "dept_emp_ibfk_1") // <-- Corrected foreign key name
    )
    private Employee employee;

    public DepartmentEmployee() {}

    public DepartmentEmployee(DepartmentEmployeeId id, Date from_date, Date to_date, Employee employee) {
        this.id = id;
        this.from_date = from_date;
        this.to_date = to_date;
        this.employee = employee;
    }

    public Integer getEmp_no() {
        return id != null ? id.getEmp_no() : null;
    }

    public void setEmp_no(Integer emp_no) {
        if (id == null) {
            id = new DepartmentEmployeeId();
        }
        id.setEmp_no(emp_no);
    }

    public String getDept_no() {
        return id != null ? id.getDept_no() : null;
    }

    public void setDept_no(String dept_no) {
        if (id == null) {
            id = new DepartmentEmployeeId();
        }
        id.setDept_no(dept_no);
    }

    public DepartmentEmployeeId getId() {
        return id;
    }

    public Date getFrom_date() {
        return from_date;
    }

    public void setFrom_date(Date from_date) {
        this.from_date = from_date;
    }

    public Date getTo_date() {
        return to_date;
    }

    public void setTo_date(Date to_date) {
        this.to_date = to_date;
    }

    public void setId(DepartmentEmployeeId id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Boolean isCurrentlyEnrolled(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getTo_date());
        return calendar.get(Calendar.YEAR) == 9999;
    }
}
