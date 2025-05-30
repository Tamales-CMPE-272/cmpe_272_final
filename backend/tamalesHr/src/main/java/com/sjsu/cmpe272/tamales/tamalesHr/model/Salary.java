package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "salaries")
public class Salary {

    @EmbeddedId
    private SalaryId id;
    private Integer salary;
    private LocalDate to_date;

    public Salary() {}

    public Salary(SalaryId id, Integer salary, LocalDate to_date) {
        this.id = id;
        this.salary = salary;
        this.to_date = to_date;
    }

    public SalaryId getId() {
        return id;
    }

    public void setId(SalaryId id) {
        this.id = id;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public LocalDate getTo_date() {
        return to_date;
    }

    public void setTo_date(LocalDate to_date) {
        this.to_date = to_date;
    }
}
