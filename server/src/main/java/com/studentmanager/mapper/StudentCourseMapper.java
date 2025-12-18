package com.studentmanager.mapper;

import com.studentmanager.entity.StudentCourse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentCourseMapper {

    @Select("SELECT * FROM student_course WHERE student_id = #{studentId} AND course_offering_id = #{courseOfferingId}")
    StudentCourse findByStudentAndOffering(@Param("studentId") Long studentId,
            @Param("courseOfferingId") Long courseOfferingId);

    @Select("SELECT * FROM student_course WHERE id = #{id}")
    StudentCourse findById(Long id);

    @Select("SELECT * FROM student_course WHERE student_id = #{studentId} AND status != 'DROPPED' ORDER BY id ASC")
    List<StudentCourse> findByStudent(Long studentId);

    @Select("SELECT * FROM student_course WHERE course_offering_id = #{courseOfferingId} AND status != 'DROPPED' ORDER BY id ASC")
    List<StudentCourse> findByOffering(Long courseOfferingId);

    @Select("SELECT * FROM student_course ORDER BY id ASC")
    List<StudentCourse> findAll();

    @Insert("INSERT INTO student_course(student_id, course_offering_id, status) VALUES(#{studentId}, #{courseOfferingId}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(StudentCourse studentCourse);

    @Update("UPDATE student_course SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("DELETE FROM student_course WHERE id = #{id}")
    void delete(Long id);
}
