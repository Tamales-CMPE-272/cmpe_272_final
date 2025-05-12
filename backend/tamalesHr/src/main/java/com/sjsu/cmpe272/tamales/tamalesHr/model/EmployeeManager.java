package com.sjsu.cmpe272.tamales.tamalesHr.model;

import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Date;

/**
 * Class used inside employee class to access manager data
 */
@Entity
@Table(name = "dept_manager")
@IdClass(DeptManagerId.class)
public class EmployeeManager {

  @Id
  @Column(name = "emp_no")
  private Integer emp_no;

  @Id
  @Column(name = "dept_no", columnDefinition = "CHAR(4)")
  private String dept_no;

  @Temporal(TemporalType.DATE)
  @Column(name = "to_date", nullable = false)
  private Date to_date;

  public EmployeeManager() {
  }

  public Integer getEmp_no() {
    return emp_no;
  }

  public void setEmp_no(Integer emp_no) {
    this.emp_no = emp_no;
  }

  public String getDept_no() {
    return dept_no;
  }

  public void setDept_no(String dept_no) {
    this.dept_no = dept_no;
  }

  public Date getTo_date() {
    return to_date;
  }

  public void setTo_date(Date to_date) {
    this.to_date = to_date;
  }

  public Boolean isCurrentlyEnrolled(){
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(getTo_date());
    return calendar.get(Calendar.YEAR) == 9999;
  }
}
