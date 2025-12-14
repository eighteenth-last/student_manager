package com.studentmanager.mapper;

import com.studentmanager.entity.OfferingScore;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OfferingScoreMapper {

    @Select("SELECT * FROM offering_score WHERE student_course_id = #{studentCourseId}")
    OfferingScore findByStudentCourseId(Long studentCourseId);

    @Select("SELECT os.*, sc.course_offering_id AS courseId, sc.student_id AS studentId " +
            "FROM offering_score os " +
            "JOIN student_course sc ON os.student_course_id = sc.id " +
            "WHERE sc.student_id = #{studentId}")
    List<OfferingScore> findByStudentId(Long studentId);

    @Select("SELECT os.*, sc.course_offering_id AS courseId, sc.student_id AS studentId " +
            "FROM offering_score os " +
            "JOIN student_course sc ON os.student_course_id = sc.id " +
            "WHERE sc.course_offering_id = #{courseOfferingId}")
    List<OfferingScore> findByCourseOfferingId(Long courseOfferingId);

    @Select("SELECT os.*, sc.course_offering_id AS courseId, sc.student_id AS studentId " +
            "FROM offering_score os " +
            "JOIN student_course sc ON os.student_course_id = sc.id")
    List<OfferingScore> findAll();

    @Insert("INSERT INTO offering_score(student_course_id, score) VALUES(#{studentCourseId}, #{score})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(OfferingScore score);

    @Update("UPDATE offering_score SET score = #{score} WHERE student_course_id = #{studentCourseId}")
    void updateScore(@Param("studentCourseId") Long studentCourseId, @Param("score") Double score);
}
