package com.sjsu.cmpe272.tamales.tamalesHr.controller;

import com.sjsu.cmpe272.tamales.tamalesHr.model.Department;
import com.sjsu.cmpe272.tamales.tamalesHr.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository repo;

    @GetMapping
    public List<Department> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable String id) {
        return repo.findById(id).orElse(null);
    }

    @PostMapping
    public Department create(@RequestBody Department dept) {
        return repo.save(dept);
    }

    @PutMapping("/{id}")
    public Department update(@PathVariable String id, @RequestBody Department dept) {
        dept.setDeptNo(id);
        return repo.save(dept);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repo.deleteById(id);
    }
}
