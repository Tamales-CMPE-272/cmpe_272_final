package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @Column(name = "dept_no", length = 4)
    private String deptNo;

    @Column(name = "dept_name", length = 40, nullable = false)
    private String deptName;
}

