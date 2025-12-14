package com.studentmanager.service.impl;

import com.studentmanager.dto.CourseRequest;
import com.studentmanager.entity.Course;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.CourseMapper;
import com.studentmanager.mapper.TeacherMapper;
import com.studentmanager.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final TeacherMapper teacherMapper;

    @Override
    public List<Course> list(int page, int size) {
        int offset = Math.max(page - 1, 0) * size;
        return courseMapper.findAll(offset, size);
    }

    @Override
    public List<Course> listByTeacher(Long teacherId) {
        return courseMapper.findByTeacherId(teacherId);
    }

    @Override
    public Course getById(Long id) {
        Course course = courseMapper.findById(id);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        return course;
    }

    @Override
    public Course create(CourseRequest request) {
        if (courseMapper.findByCourseNo(request.getCourseNo()) != null) {
            throw new BusinessException("课程号已存在");
        }
        if (teacherMapper.findById(request.getTeacherId()) == null) {
            throw new BusinessException("授课教师不存在");
        }
        Course course = toEntity(request);
        courseMapper.insert(course);
        return course;
    }

    @Override
    public Course update(Long id, CourseRequest request) {
        Course course = getById(id);
        course.setCourseName(request.getCourseName());
        course.setCredit(request.getCredit());
        course.setTeacherId(request.getTeacherId());
        course.setClassTime(request.getClassTime());
        courseMapper.update(course);
        return course;
    }

    @Override
    public void delete(Long id) {
        Course course = getById(id);
        courseMapper.delete(course.getId());
    }

    private Course toEntity(CourseRequest request) {
        Course course = new Course();
        course.setCourseNo(request.getCourseNo());
        course.setCourseName(request.getCourseName());
        course.setCredit(request.getCredit());
        course.setTeacherId(request.getTeacherId());
        course.setClassTime(request.getClassTime());
        return course;
    }
}
