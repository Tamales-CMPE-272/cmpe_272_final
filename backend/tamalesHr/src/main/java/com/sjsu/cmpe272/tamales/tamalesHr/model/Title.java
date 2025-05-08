package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "titles")
public class Title {

    @EmbeddedId
    private TitleId id;

    private Date to_date;

    public Title() {}

    public Integer getEmp_no() {
        return id != null ? id.getEmp_no() : null;
    }

    public void setEmp_no(Integer emp_no) {
        if (id == null) {
            id = new TitleId();
        }
        id.setEmp_no(emp_no);
    }

    public String getTitle() {
        return id != null ? id.getTitle() : null;
    }

    public void setTitle(String title) {
        if (id == null) {
            id = new TitleId();
        }
        id.setTitle(title);
    }

    public Date getFrom_date() {
        return id != null ? id.getFrom_date() : null;
    }

    public void setFrom_date(Date from_date) {
        if (id == null) {
            id = new TitleId();
        }
        id.setFrom_date(from_date);
    }

    public Date getTo_date() {
        return to_date;
    }

    public void setTo_date(Date to_date) {
        this.to_date = to_date;
    }
}
