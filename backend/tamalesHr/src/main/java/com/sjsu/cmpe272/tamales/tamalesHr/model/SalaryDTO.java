package com.sjsu.cmpe272.tamales.tamalesHr.model;

import java.time.LocalDate;

public class SalaryDTO {

    private Integer empNo;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer salary;

    public SalaryDTO() {}

    public SalaryDTO(Integer empNo, LocalDate fromDate, LocalDate toDate, Integer salary) {
        this.empNo = empNo;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.salary = salary;
    }

    public Integer getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Integer empNo) {
        this.empNo = empNo;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
