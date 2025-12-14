package com.studentmanager.service.impl;

import com.studentmanager.dto.TeacherRequest;
import com.studentmanager.entity.SysUser;
import com.studentmanager.entity.Teacher;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.SysUserMapper;
import com.studentmanager.mapper.TeacherMapper;
import com.studentmanager.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherMapper teacherMapper;
    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Teacher> list(int page, int size) {
        int offset = Math.max(page - 1, 0) * size;
        return teacherMapper.findAll(offset, size);
    }

    @Override
    public Teacher getById(Long id) {
        Teacher teacher = teacherMapper.findById(id);
        if (teacher == null) {
            throw new BusinessException("教师不存在");
        }
        return teacher;
    }

    @Override
    public Teacher create(TeacherRequest request) {
        if (teacherMapper.findByTeacherNo(request.getTeacherNo()) != null) {
            throw new BusinessException("教师工号已存在");
        }
        Teacher teacher = toEntity(request);
        teacherMapper.insert(teacher);
        createUserIfAbsent(teacher);
        return teacher;
    }

    @Override
    public Teacher update(Long id, TeacherRequest request) {
        Teacher teacher = getById(id);
        teacher.setName(request.getName());
        teacher.setPhone(request.getPhone());
        teacher.setDepartment(request.getDepartment());
        teacherMapper.update(teacher);
        return teacher;
    }

    @Override
    public void delete(Long id) {
        Teacher teacher = getById(id);
        teacherMapper.delete(teacher.getId());
    }

    private Teacher toEntity(TeacherRequest request) {
        Teacher teacher = new Teacher();
        teacher.setTeacherNo(request.getTeacherNo());
        teacher.setName(request.getName());
        teacher.setPhone(request.getPhone());
        teacher.setDepartment(request.getDepartment());
        return teacher;
    }

    private void createUserIfAbsent(Teacher teacher) {
        String username = teacher.getTeacherNo();
        SysUser existingUser = sysUserMapper.findByUsername(username);
        if (existingUser != null) {
            return;
        }
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole("TEACHER");
        user.setRelatedId(teacher.getId());
        user.setStatus(1);
        sysUserMapper.insert(user);
    }
}
