package com.sjsu.cmpe272.tamales.tamalesHr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SalaryDTO {
    private Long empNo;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Double salary;
}