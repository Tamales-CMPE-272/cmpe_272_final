package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.Date;

@Embeddable
public class TitleId implements Serializable {
    private Integer emp_no;
    private String title;
    private Date from_date;

    public TitleId() {}

    public TitleId(Integer emp_no, String title, Date from_date) {
        this.emp_no = emp_no;
        this.title = title;
        this.from_date = from_date;
    }

    public Integer getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(Integer emp_no) {
        this.emp_no = emp_no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getFrom_date() {
        return from_date;
    }

    public void setFrom_date(Date from_date) {
        this.from_date = from_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TitleId)) return false;
        TitleId titleId = (TitleId) o;
        return Objects.equals(emp_no, titleId.emp_no) && 
               Objects.equals(title, titleId.title) && 
               Objects.equals(from_date, titleId.from_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emp_no, title, from_date);
    }
}
