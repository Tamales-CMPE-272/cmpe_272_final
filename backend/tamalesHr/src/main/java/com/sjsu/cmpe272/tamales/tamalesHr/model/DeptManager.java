package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "dept_manager")
@IdClass(DeptManagerId.class)
public class DeptManager {

    @Id
    @Column(name = "emp_no")
    private Integer emp_no;

    @Id
    @Column(name = "dept_no", columnDefinition = "CHAR(4)")
    private String dept_no;

    @Temporal(TemporalType.DATE)
    @Column(name = "from_date", nullable = false)
    private Date from_date;

    @Temporal(TemporalType.DATE)
    @Column(name = "to_date", nullable = false)
    private Date to_date;

    @ManyToOne
    @JoinColumn(
            name = "emp_no",
            referencedColumnName = "emp_no",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "dept_manager_ibfk_1")  // optional, for clarity
    )
    private Employee employee;

    @ManyToOne
    @JoinColumn(
            name = "dept_no",
            referencedColumnName = "dept_no",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "dept_manager_ibfk_2")  // 💡 this is the key update
    )
    private Department department;

    public DeptManager() {}

    // Getters and setters

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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Boolean isCurrentlyEnrolled(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getTo_date());
        return calendar.get(Calendar.YEAR) == 9999;
    }
}
