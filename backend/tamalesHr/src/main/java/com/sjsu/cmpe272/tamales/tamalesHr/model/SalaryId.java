package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Embeddable
public class SalaryId implements Serializable {
    @Column(name = "emp_no")
    private Long empNo;
    @Column(name = "from_date")
    private LocalDate from_date;
}
