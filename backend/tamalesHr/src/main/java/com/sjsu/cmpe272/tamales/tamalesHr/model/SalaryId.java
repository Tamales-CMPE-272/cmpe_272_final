package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class SalaryId implements Serializable {

    @Column(name = "emp_no")
    private Integer empNo;

    @Column(name = "from_date")
    private LocalDate from_date;

    public SalaryId() {}

    public SalaryId(Integer empNo, LocalDate from_date) {
        this.empNo = empNo;
        this.from_date = from_date;
    }

    public Integer getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Integer empNo) {
        this.empNo = empNo;
    }

    public LocalDate getFrom_date() {
        return from_date;
    }

    public void setFrom_date(LocalDate from_date) {
        this.from_date = from_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalaryId)) return false;

        SalaryId that = (SalaryId) o;

        if (!empNo.equals(that.empNo)) return false;
        return from_date.equals(that.from_date);
    }

    @Override
    public int hashCode() {
        int result = empNo.hashCode();
        result = 31 * result + from_date.hashCode();
        return result;
    }
}
