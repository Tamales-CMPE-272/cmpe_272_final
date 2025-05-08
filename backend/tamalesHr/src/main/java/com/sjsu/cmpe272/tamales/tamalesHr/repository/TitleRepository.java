package com.sjsu.cmpe272.tamales.tamalesHr.repository;

import com.sjsu.cmpe272.tamales.tamalesHr.model.Title;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface TitleRepository extends JpaRepository<Title, Long> {

    @Query("SELECT t FROM Title t WHERE t.id.emp_no = :emp_no ORDER BY t.to_date DESC")
    List<Title> findByEmpNoOrderByToDateDesc(@Param("emp_no") Integer emp_no);
}
