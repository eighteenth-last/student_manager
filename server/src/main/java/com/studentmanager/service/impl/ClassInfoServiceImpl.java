package com.studentmanager.service.impl;

import com.studentmanager.dto.ClassInfoRequest;
import com.studentmanager.entity.ClassInfo;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.ClassInfoMapper;
import com.studentmanager.service.ClassInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassInfoServiceImpl implements ClassInfoService {

    private final ClassInfoMapper classInfoMapper;

    @Override
    public List<ClassInfo> list(int page, int size) {
        int offset = Math.max(page - 1, 0) * size;
        return classInfoMapper.findAll(offset, size);
    }

    @Override
    public ClassInfo getById(Long id) {
        ClassInfo info = classInfoMapper.findById(id);
        if (info == null) {
            throw new BusinessException("班级不存在");
        }
        return info;
    }

    @Override
    public ClassInfo create(ClassInfoRequest request) {
        if (classInfoMapper.findByClassCode(request.getClassCode()) != null) {
            throw new BusinessException("班级编号已存在");
        }
        ClassInfo info = toEntity(request);
        classInfoMapper.insert(info);
        return info;
    }

    @Override
    public ClassInfo update(Long id, ClassInfoRequest request) {
        ClassInfo info = getById(id);
        info.setMajor(request.getMajor());
        info.setHeadTeacher(request.getHeadTeacher());
        info.setEntryYear(request.getEntryYear());
        classInfoMapper.update(info);
        return info;
    }

    @Override
    public void delete(Long id) {
        ClassInfo info = getById(id);
        classInfoMapper.delete(info.getId());
    }

    private ClassInfo toEntity(ClassInfoRequest request) {
        ClassInfo info = new ClassInfo();
        info.setClassCode(request.getClassCode());
        info.setMajor(request.getMajor());
        info.setHeadTeacher(request.getHeadTeacher());
        info.setEntryYear(request.getEntryYear());
        return info;
    }
}
