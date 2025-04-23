package com.sjsu.cmpe272.tamales.tamalesHr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptManagerId implements Serializable {
    private Integer empNo;
    private String deptNo;
}

