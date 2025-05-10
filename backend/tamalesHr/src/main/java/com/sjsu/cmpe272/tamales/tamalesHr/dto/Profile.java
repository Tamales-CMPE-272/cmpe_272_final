package com.sjsu.cmpe272.tamales.tamalesHr.dto;

import com.sjsu.cmpe272.tamales.tamalesHr.model.Gender;

import java.util.List;
import java.util.Date;

public class Profile {
    private Integer emp_no;
    private String first_name;
    private String last_name;
    private Gender gender;
    private Date birth_date;
    private Date hire_date;
    private List<String> titles;
    private List<String> department_names;

    public Profile() {}

    public Profile(Integer emp_no, String first_name, String last_name, Gender gender, Date birth_date, Date hire_date, List<String> titles, List<String> department_names) {
        this.emp_no = emp_no;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.birth_date = birth_date;
        this.hire_date = hire_date;
        this.titles = titles;
        this.department_names = department_names;
    }

    public Integer getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(Integer emp_no) {
        this.emp_no = emp_no;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public Date getHire_date() {
        return hire_date;
    }

    public void setHire_date(Date hire_date) {
        this.hire_date = hire_date;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getDepartment_names() {
        return department_names;
    }

    public void setDepartment_names(List<String> department_names) {
        this.department_names = department_names;
    }
}


