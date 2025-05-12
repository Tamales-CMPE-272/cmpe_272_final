package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer emp_no;
    @Temporal(TemporalType.DATE)
    private Date birth_date;
    private String first_name;
    private String last_name;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, columnDefinition = "ENUM('M','F')")
    private Gender gender;
    @Temporal(TemporalType.DATE)
    private Date hire_date;

    @OneToMany
    @JoinColumn(
            name = "emp_no", // Column in dept_manager table
            referencedColumnName = "emp_no" // Column in employees table (PK)
    )
    private List<EmployeeManager> deptManagers;

    public Employee() {
    }

    public Integer getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(Integer emp_no) {
        this.emp_no = emp_no;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Date getHire_date() {
        return hire_date;
    }

    public void setHire_date(Date hire_date) {
        this.hire_date = hire_date;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<EmployeeManager> getDeptManagers() {
        return deptManagers;
    }

    public void setDeptManagers(List<EmployeeManager> deptManagers) {
        this.deptManagers = deptManagers;
    }
}
