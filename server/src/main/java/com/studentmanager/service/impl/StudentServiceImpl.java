package com.studentmanager.service.impl;

import com.studentmanager.dto.StudentRequest;
import com.studentmanager.entity.Student;
import com.studentmanager.entity.SysUser;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.StudentMapper;
import com.studentmanager.mapper.SysUserMapper;
import com.studentmanager.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;
    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Student> list(int page, int size) {
        int offset = Math.max(page - 1, 0) * size;
        return studentMapper.findAll(offset, size);
    }

    @Override
    public Student getById(Long id) {
        Student student = studentMapper.findById(id);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }
        return student;
    }

    @Override
    public Student create(StudentRequest request) {
        if (studentMapper.findByStudentNo(request.getStudentNo()) != null) {
            throw new BusinessException("学号已存在");
        }
        Student student = toEntity(request);
        studentMapper.insert(student);
        createUserIfAbsent(student);
        return student;
    }

    @Override
    public Student update(Long id, StudentRequest request) {
        Student existing = getById(id);
        existing.setName(request.getName());
        existing.setAge(request.getAge());
        existing.setGender(request.getGender());
        existing.setPhone(request.getPhone());
        existing.setDepartment(request.getDepartment());
        existing.setMajor(request.getMajor());
        existing.setClassId(request.getClassId());
        studentMapper.update(existing);
        return existing;
    }

    @Override
    public void delete(Long id) {
        Student student = getById(id);
        studentMapper.delete(student.getId());
    }

    private Student toEntity(StudentRequest request) {
        Student s = new Student();
        s.setStudentNo(request.getStudentNo());
        s.setName(request.getName());
        s.setAge(request.getAge());
        s.setGender(request.getGender());
        s.setPhone(request.getPhone());
        s.setDepartment(request.getDepartment());
        s.setMajor(request.getMajor());
        s.setClassId(request.getClassId());
        return s;
    }

    private void createUserIfAbsent(Student student) {
        String username = student.getStudentNo();
        SysUser existingUser = sysUserMapper.findByUsername(username);
        if (existingUser != null) {
            return;
        }
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole("STUDENT");
        user.setRelatedId(student.getId());
        user.setStatus(1);
        sysUserMapper.insert(user);
    }
}
