package com.studentmanager.service.impl;

import com.studentmanager.dto.ScoreRequest;
import com.studentmanager.dto.ScoreResponse;
import com.studentmanager.entity.*;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.*;
import com.studentmanager.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final OfferingScoreMapper offeringScoreMapper;
    private final EnrollmentScoreMapper enrollmentScoreMapper;
    private final StudentCourseMapper studentCourseMapper;
    private final CourseOfferingMapper courseOfferingMapper;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;

    @Override
    public ScoreResponse recordOfferingScore(ScoreRequest request) {
        // request.getCourseId() 是 courseOfferingId
        StudentCourse sc = studentCourseMapper.findByStudentAndOffering(request.getStudentId(), request.getCourseId());
        if (sc == null) {
            throw new BusinessException("选课记录不存在，无法录入成绩");
        }
        OfferingScore existing = offeringScoreMapper.findByStudentCourseId(sc.getId());
        if (existing == null) {
            OfferingScore score = new OfferingScore();
            score.setStudentCourseId(sc.getId());
            score.setScore(request.getScore());
            offeringScoreMapper.insert(score);
            return buildOfferingScoreResponse(score, sc);
        }
        offeringScoreMapper.updateScore(sc.getId(), request.getScore());
        existing.setScore(request.getScore());
        return buildOfferingScoreResponse(existing, sc);
    }

    @Override
    public ScoreResponse recordEnrollmentScore(ScoreRequest request) {
        // request.getCourseId() 是公共课程ID，公共课所有学生都可以查看
        EnrollmentScore existing = enrollmentScoreMapper.findByStudentAndCourse(request.getStudentId(), request.getCourseId());
        if (existing == null) {
            EnrollmentScore score = new EnrollmentScore();
            score.setStudentId(request.getStudentId());
            score.setCourseId(request.getCourseId());
            score.setScore(request.getScore());
            enrollmentScoreMapper.insert(score);
            return buildEnrollmentScoreResponse(score);
        }
        enrollmentScoreMapper.updateScore(request.getStudentId(), request.getCourseId(), request.getScore());
        existing.setScore(request.getScore());
        return buildEnrollmentScoreResponse(existing);
    }

    @Override
    public List<ScoreResponse> listByStudent(Long studentId) {
        List<ScoreResponse> result = new ArrayList<>();
        
        // 获取选课成绩
        List<StudentCourse> studentCourses = studentCourseMapper.findByStudent(studentId);
        for (StudentCourse sc : studentCourses) {
            OfferingScore score = offeringScoreMapper.findByStudentCourseId(sc.getId());
            if (score != null) {
                result.add(buildOfferingScoreResponse(score, sc));
            }
        }
        
        // 获取公共课成绩
        List<EnrollmentScore> enrollmentScores = enrollmentScoreMapper.findByStudentId(studentId);
        for (EnrollmentScore score : enrollmentScores) {
            result.add(buildEnrollmentScoreResponse(score));
        }
        
        return result;
    }

    @Override
    public List<ScoreResponse> listByCourse(Long courseId, String type) {
        List<ScoreResponse> result = new ArrayList<>();
        
        if ("OFFERING".equals(type)) {
            List<StudentCourse> studentCourses = studentCourseMapper.findByOffering(courseId);
            for (StudentCourse sc : studentCourses) {
                OfferingScore score = offeringScoreMapper.findByStudentCourseId(sc.getId());
                if (score != null) {
                    result.add(buildOfferingScoreResponse(score, sc));
                }
            }
        } else if ("ENROLLMENT".equals(type)) {
            List<EnrollmentScore> enrollmentScores = enrollmentScoreMapper.findByCourseId(courseId);
            for (EnrollmentScore score : enrollmentScores) {
                result.add(buildEnrollmentScoreResponse(score));
            }
        }
        
        return result;
    }

    @Override
    public List<ScoreResponse> listAll(String type) {
        List<ScoreResponse> result = new ArrayList<>();
        
        if ("OFFERING".equals(type)) {
            // 获取所有选课成绩
            List<StudentCourse> allStudentCourses = studentCourseMapper.findAll();
            for (StudentCourse sc : allStudentCourses) {
                OfferingScore score = offeringScoreMapper.findByStudentCourseId(sc.getId());
                if (score != null) {
                    result.add(buildOfferingScoreResponse(score, sc));
                }
            }
        } else if ("ENROLLMENT".equals(type)) {
            // 获取所有公共课成绩
            List<EnrollmentScore> allEnrollmentScores = enrollmentScoreMapper.findAll();
            for (EnrollmentScore score : allEnrollmentScores) {
                result.add(buildEnrollmentScoreResponse(score));
            }
        }
        
        return result;
    }

    private ScoreResponse buildOfferingScoreResponse(OfferingScore score, StudentCourse sc) {
        ScoreResponse response = new ScoreResponse();
        response.setId(score.getId());
        response.setType("OFFERING");
        response.setScore(score.getScore());
        response.setUpdatedAt(score.getUpdatedAt());
        
        // 获取课程信息
        CourseOffering offering = courseOfferingMapper.findById(sc.getCourseOfferingId());
        if (offering != null) {
            response.setCourseId(offering.getId());
            response.setCourseNo(offering.getCourseNo());
            response.setCourseName(offering.getCourseName());
            response.setSemester(offering.getSemester());
            
            // 获取教师信息
            Teacher teacher = teacherMapper.findById(offering.getTeacherId());
            if (teacher != null) {
                response.setTeacherName(teacher.getName());
            }
        }
        
        // 获取学生信息
        Student student = studentMapper.findById(sc.getStudentId());
        if (student != null) {
            response.setStudentId(student.getId());
            response.setStudentNo(student.getStudentNo());
            response.setStudentName(student.getName());
        }
        
        return response;
    }

    private ScoreResponse buildEnrollmentScoreResponse(EnrollmentScore score) {
        ScoreResponse response = new ScoreResponse();
        response.setId(score.getId());
        response.setType("ENROLLMENT");
        response.setScore(score.getScore());
        response.setUpdatedAt(score.getUpdatedAt());
        
        // 获取公共课程信息
        Course course = courseMapper.findById(score.getCourseId());
        if (course != null) {
            response.setCourseId(course.getId());
            response.setCourseNo(course.getCourseNo());
            response.setCourseName(course.getCourseName());
            
            // 获取教师信息
            if (course.getTeacherId() != null) {
                Teacher teacher = teacherMapper.findById(course.getTeacherId());
                if (teacher != null) {
                    response.setTeacherName(teacher.getName());
                }
            }
        }
        
        // 获取学生信息
        Student student = studentMapper.findById(score.getStudentId());
        if (student != null) {
            response.setStudentId(student.getId());
            response.setStudentNo(student.getStudentNo());
            response.setStudentName(student.getName());
        }
        
        return response;
    }

    @Override
    public List<Object> getStudentsByCourse(Long courseId, String type) {
        List<Object> students = new ArrayList<>();
        
        if ("OFFERING".equals(type)) {
            // 选课成绩：获取已选该课程开课的学生
            List<StudentCourse> studentCourses = studentCourseMapper.findByOffering(courseId);
            for (StudentCourse sc : studentCourses) {
                Student student = studentMapper.findById(sc.getStudentId());
                if (student != null) {
                    students.add(new java.util.HashMap<String, Object>() {{
                        put("id", student.getId());
                        put("studentNo", student.getStudentNo());
                        put("name", student.getName());
                    }});
                }
            }
        } else if ("ENROLLMENT".equals(type)) {
            // 公共课成绩：返回所有学生（公共课所有学生可见）
            List<Student> allStudents = studentMapper.findAll(0, 10000);
            for (Student student : allStudents) {
                students.add(new java.util.HashMap<String, Object>() {{
                    put("id", student.getId());
                    put("studentNo", student.getStudentNo());
                    put("name", student.getName());
                }});
            }
        }
        
        return students;
    }

    @Override
    public Object getTeacherCourses(Long teacherId, String type) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        
        if ("OFFERING".equals(type)) {
            // 获取教师的开课课程
            List<CourseOffering> offerings;
            if (teacherId == null) {
                // 管理员：所有课程
                offerings = courseOfferingMapper.findAll(0, 10000);
            } else {
                // 教师：只获取自己的课程
                offerings = courseOfferingMapper.findByTeacherId(teacherId);
            }
            
            List<java.util.Map<String, Object>> courses = new ArrayList<>();
            for (CourseOffering offering : offerings) {
                courses.add(new java.util.HashMap<String, Object>() {{
                    put("id", offering.getId());
                    put("courseNo", offering.getCourseNo());
                    put("courseName", offering.getCourseName());
                    put("semester", offering.getSemester());
                    put("teacherName", offering.getTeacherName());
                }});
            }
            result.put("courses", courses);
        } else if ("ENROLLMENT".equals(type)) {
            // 获取教师的公共课程
            List<Course> publicCourses;
            if (teacherId == null) {
                // 管理员：所有公共课
                publicCourses = courseMapper.findAll(0, 10000);
            } else {
                // 教师：只获取自己的公共课
                publicCourses = courseMapper.findByTeacherId(teacherId);
            }
            
            List<java.util.Map<String, Object>> courses = new ArrayList<>();
            for (Course course : publicCourses) {
                Teacher teacher = null;
                if (course.getTeacherId() != null) {
                    teacher = teacherMapper.findById(course.getTeacherId());
                }
                final String teacherName = teacher != null ? teacher.getName() : "";
                courses.add(new java.util.HashMap<String, Object>() {{
                    put("id", course.getId());
                    put("courseNo", course.getCourseNo());
                    put("courseName", course.getCourseName());
                    put("teacherName", teacherName);
                }});
            }
            result.put("courses", courses);
        }
        
        return result;
    }
}
