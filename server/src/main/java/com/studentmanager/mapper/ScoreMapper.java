package com.studentmanager.mapper;

import com.studentmanager.entity.Score;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ScoreMapper {

    @Select("SELECT * FROM score WHERE student_course_id = #{studentCourseId}")
    Score findByStudentCourseId(Long studentCourseId);

    @Select("SELECT s.*, sc.course_offering_id AS courseId, sc.student_id AS studentId FROM score s JOIN student_course sc ON s.student_course_id = sc.id WHERE sc.student_id = #{studentId}")
    java.util.List<Score> findByStudentId(Long studentId);

    @Select("SELECT s.*, sc.course_offering_id AS courseId, sc.student_id AS studentId FROM score s JOIN student_course sc ON s.student_course_id = sc.id JOIN course_offering co ON sc.course_offering_id = co.id WHERE co.id = #{courseOfferingId}")
    java.util.List<Score> findByCourseId(Long courseOfferingId);

    @Select("SELECT s.*, sc.course_offering_id AS courseId, sc.student_id AS studentId FROM score s JOIN student_course sc ON s.student_course_id = sc.id")
    java.util.List<Score> findAll();

    @Insert("INSERT INTO score(student_course_id, score) VALUES(#{studentCourseId}, #{score})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Score score);

    @Update("UPDATE score SET score = #{score} WHERE student_course_id = #{studentCourseId}")
    void updateScore(@Param("studentCourseId") Long studentCourseId, @Param("score") Double score);
}
