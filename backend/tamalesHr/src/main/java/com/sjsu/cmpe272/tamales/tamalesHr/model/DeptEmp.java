package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "dept_emp")
@IdClass(DeptEmp.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptEmp {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Id
    @Column(name = "dept_no", length = 4)
    private String deptNo;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;
}
