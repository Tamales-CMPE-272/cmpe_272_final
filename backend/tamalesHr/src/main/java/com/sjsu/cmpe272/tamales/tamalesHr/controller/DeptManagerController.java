package com.sjsu.cmpe272.tamales.tamalesHr.controller;

import com.sjsu.cmpe272.tamales.tamalesHr.model.DeptManager;
import com.sjsu.cmpe272.tamales.tamalesHr.repository.DeptManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dept-manager")
public class DeptManagerController {

    @Autowired
    private DeptManagerRepository repo;

    @GetMapping
    public List<DeptManager> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public DeptManager create(@RequestBody DeptManager dm) {
        return repo.save(dm);
    }

    @PutMapping
    public DeptManager update(@RequestBody DeptManager dm) {
        return repo.save(dm);
    }

    @DeleteMapping("/{empNo}/{deptNo}")
    public void delete(@PathVariable int empNo, @PathVariable String deptNo) {
        repo.deleteById(empNo);
    }
}
