package com.studentmanager.service.impl;

import com.studentmanager.entity.CourseOffering;
import com.studentmanager.entity.Student;
import com.studentmanager.entity.StudentCourse;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.CourseOfferingMapper;
import com.studentmanager.mapper.StudentCourseMapper;
import com.studentmanager.mapper.StudentMapper;
import com.studentmanager.service.CourseOfferingService;
import com.studentmanager.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final StudentCourseMapper studentCourseMapper;
    private final StudentMapper studentMapper;
    private final CourseOfferingMapper courseOfferingMapper;
    private final CourseOfferingService courseOfferingService;

    @Override
    @Transactional
    public StudentCourse enroll(Long studentId, Long courseId) {
        // courseId现在是courseOfferingId
        Student student = studentMapper.findById(studentId);
        CourseOffering offering = courseOfferingMapper.findById(courseId);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }
        if (offering == null) {
            throw new BusinessException("开课信息不存在");
        }
        if (!"OPEN".equals(offering.getStatus())) {
            throw new BusinessException("该课程不在选课期间");
        }
        if (offering.getCurrentStudents() >= offering.getMaxStudents()) {
            throw new BusinessException("该课程选课人数已满");
        }
        if (studentCourseMapper.findByStudentAndOffering(studentId, courseId) != null) {
            throw new BusinessException("已选该课程");
        }
        StudentCourse sc = new StudentCourse();
        sc.setStudentId(studentId);
        sc.setCourseOfferingId(courseId);
        sc.setStatus("ENROLLED");
        studentCourseMapper.insert(sc);
        courseOfferingService.incrementStudentCount(courseId);
        return sc;
    }

    @Override
    public List<StudentCourse> listByStudent(Long studentId) {
        return studentCourseMapper.findByStudent(studentId);
    }

    @Override
    public List<StudentCourse> listByCourse(Long courseId) {
        // courseId现在是courseOfferingId
        if (courseId == 0) {
            return studentCourseMapper.findAll();
        }
        return studentCourseMapper.findByOffering(courseId);
    }

    @Override
    public List<StudentCourse> listAll() {
        return studentCourseMapper.findAll();
    }

    @Override
    @Transactional
    public void drop(Long enrollmentId) {
        StudentCourse sc = studentCourseMapper.findById(enrollmentId);
        if (sc == null) {
            throw new BusinessException("选课记录不存在");
        }
        if ("DROPPED".equals(sc.getStatus())) {
            throw new BusinessException("已退选该课程");
        }
        studentCourseMapper.updateStatus(enrollmentId, "DROPPED");
        courseOfferingService.decrementStudentCount(sc.getCourseOfferingId());
    }
}
