package com.studentmanager.service.impl;

import com.studentmanager.entity.Major;
import com.studentmanager.mapper.MajorMapper;
import com.studentmanager.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    private final MajorMapper majorMapper;

    @Override
    public List<Major> listAll() {
        return majorMapper.findAll();
    }

    @Override
    public List<Major> listByDepartment(Long departmentId) {
        return majorMapper.findByDepartmentId(departmentId);
    }

    @Override
    public Major getById(Long id) {
        return majorMapper.findById(id);
    }

    @Override
    public Major create(Major major) {
        majorMapper.insert(major);
        return major;
    }

    @Override
    public Major update(Long id, Major major) {
        major.setId(id);
        majorMapper.update(major);
        return majorMapper.findById(id);
    }

    @Override
    public void delete(Long id) {
        majorMapper.delete(id);
    }
}
