package com.studentmanager.service;

import com.studentmanager.entity.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> listAll();
    
    Department getById(Long id);
    
    Department create(Department department);
    
    Department update(Long id, Department department);
    
    void delete(Long id);
}
