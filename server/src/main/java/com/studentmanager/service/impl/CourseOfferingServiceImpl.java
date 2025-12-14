package com.studentmanager.service.impl;

import com.studentmanager.entity.CourseOffering;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.CourseOfferingMapper;
import com.studentmanager.service.CourseOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseOfferingServiceImpl implements CourseOfferingService {

    private final CourseOfferingMapper courseOfferingMapper;

    @Override
    public List<CourseOffering> list(int page, int size) {
        int offset = Math.max(page - 1, 0) * size;
        return courseOfferingMapper.findAll(offset, size);
    }

    @Override
    public List<CourseOffering> listBySemester(String semester) {
        return courseOfferingMapper.findBySemester(semester);
    }

    @Override
    public List<CourseOffering> listOpenOfferings(String semester) {
        if (semester == null || semester.trim().isEmpty()) {
            return courseOfferingMapper.findAllOpen();
        }
        return courseOfferingMapper.findBySemesterAndStatus(semester, "OPEN", 0, 1000);
    }

    @Override
    public List<CourseOffering> listByTeacherId(Long teacherId) {
        return courseOfferingMapper.findByTeacherId(teacherId);
    }

    @Override
    public CourseOffering getById(Long id) {
        CourseOffering offering = courseOfferingMapper.findById(id);
        if (offering == null) {
            throw new BusinessException("开课记录不存在");
        }
        return offering;
    }

    @Override
    @Transactional
    public CourseOffering create(CourseOffering offering) {
        if (offering.getMaxStudents() == null) {
            offering.setMaxStudents(100);
        }
        if (offering.getCurrentStudents() == null) {
            offering.setCurrentStudents(0);
        }
        if (offering.getStatus() == null) {
            offering.setStatus("OPEN");
        }
        courseOfferingMapper.insert(offering);
        return getById(offering.getId());
    }

    @Override
    @Transactional
    public CourseOffering update(Long id, CourseOffering offering) {
        CourseOffering existing = getById(id);
        offering.setId(id);
        courseOfferingMapper.update(offering);
        return getById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getById(id);
        courseOfferingMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void incrementStudentCount(Long id) {
        courseOfferingMapper.incrementCurrentStudents(id);
    }

    @Override
    @Transactional
    public void decrementStudentCount(Long id) {
        courseOfferingMapper.decrementCurrentStudents(id);
    }
}
