package com.studentmanager.mapper;

import com.studentmanager.entity.EnrollmentScore;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EnrollmentScoreMapper {

    @Select("SELECT * FROM enrollment_score WHERE student_id = #{studentId} AND course_id = #{courseId}")
    EnrollmentScore findByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Select("SELECT * FROM enrollment_score WHERE student_id = #{studentId}")
    List<EnrollmentScore> findByStudentId(Long studentId);

    @Select("SELECT * FROM enrollment_score WHERE course_id = #{courseId}")
    List<EnrollmentScore> findByCourseId(Long courseId);

    @Select("SELECT * FROM enrollment_score")
    List<EnrollmentScore> findAll();

    @Insert("INSERT INTO enrollment_score(student_id, course_id, score) VALUES(#{studentId}, #{courseId}, #{score})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(EnrollmentScore score);

    @Update("UPDATE enrollment_score SET score = #{score} WHERE student_id = #{studentId} AND course_id = #{courseId}")
    void updateScore(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("score") Double score);
}
