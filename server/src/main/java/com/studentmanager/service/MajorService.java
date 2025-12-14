package com.studentmanager.service;

import com.studentmanager.entity.Major;

import java.util.List;

public interface MajorService {
    List<Major> listAll();
    
    List<Major> listByDepartment(Long departmentId);
    
    Major getById(Long id);
    
    Major create(Major major);
    
    Major update(Long id, Major major);
    
    void delete(Long id);
}
