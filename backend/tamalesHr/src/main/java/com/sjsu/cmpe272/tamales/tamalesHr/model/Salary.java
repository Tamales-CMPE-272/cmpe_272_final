package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Entity
@Table(name = "salaries")
@Data
public class Salary {
    @EmbeddedId
    private SalaryId id;
    private Double salary;
    private LocalDate to_date;
}
