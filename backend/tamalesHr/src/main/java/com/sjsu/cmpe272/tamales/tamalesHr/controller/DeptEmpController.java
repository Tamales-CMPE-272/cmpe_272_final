package com.sjsu.cmpe272.tamales.tamalesHr.controller;

import com.sjsu.cmpe272.tamales.tamalesHr.model.DeptEmp;
import com.sjsu.cmpe272.tamales.tamalesHr.repository.DeptEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dept-emp")
public class DeptEmpController {

    @Autowired
    private DeptEmpRepository repo;

    @GetMapping
    public List<DeptEmp> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public DeptEmp create(@RequestBody DeptEmp deptEmp) {
        return repo.save(deptEmp);
    }

    @PutMapping
    public DeptEmp update(@RequestBody DeptEmp deptEmp) {
        return repo.save(deptEmp);
    }

    @DeleteMapping("/{empNo}/{deptNo}")
    public void delete(@PathVariable int empNo, @PathVariable String deptNo) {
        repo.deleteById(empNo);
    }
}

