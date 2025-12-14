package com.studentmanager.service.impl;

import com.studentmanager.entity.Department;
import com.studentmanager.mapper.DepartmentMapper;
import com.studentmanager.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;

    @Override
    public List<Department> listAll() {
        return departmentMapper.findAll();
    }

    @Override
    public Department getById(Long id) {
        return departmentMapper.findById(id);
    }

    @Override
    public Department create(Department department) {
        departmentMapper.insert(department);
        return department;
    }

    @Override
    public Department update(Long id, Department department) {
        department.setId(id);
        departmentMapper.update(department);
        return departmentMapper.findById(id);
    }

    @Override
    public void delete(Long id) {
        departmentMapper.delete(id);
    }
}
