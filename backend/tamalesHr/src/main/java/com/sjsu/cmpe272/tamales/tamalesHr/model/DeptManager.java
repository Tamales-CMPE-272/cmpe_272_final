package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "dept_manager")
@IdClass(DeptManagerId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptManager {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Id
    @Column(name = "dept_no", length = 4)
    private String deptNo;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;
}

