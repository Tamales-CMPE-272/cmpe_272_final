package com.sjsu.cmpe272.tamales.tamalesHr.model;

import java.time.LocalDate;

public class SalaryDTO {

    private Long empNo;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Double salary;

    public SalaryDTO() {}

    public SalaryDTO(Long empNo, LocalDate fromDate, LocalDate toDate, Double salary) {
        this.empNo = empNo;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.salary = salary;
    }

    public Long getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Long empNo) {
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

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
