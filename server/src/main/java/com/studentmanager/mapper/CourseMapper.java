package com.studentmanager.mapper;

import com.studentmanager.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    @Select("SELECT * FROM course WHERE id = #{id}")
    Course findById(Long id);

    @Select("SELECT * FROM course WHERE course_no = #{courseNo}")
    Course findByCourseNo(String courseNo);

    @Select("SELECT * FROM course WHERE teacher_id = #{teacherId} ORDER BY id")
    List<Course> findByTeacherId(Long teacherId);

    @Select("SELECT * FROM course ORDER BY id LIMIT #{offset}, #{limit}")
    List<Course> findAll(@Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO course(course_no, course_name, credit, teacher_id, class_time) VALUES(#{courseNo}, #{courseName}, #{credit}, #{teacherId}, #{classTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Course course);

    @Update("UPDATE course SET course_name=#{courseName}, credit=#{credit}, teacher_id=#{teacherId}, class_time=#{classTime} WHERE id=#{id}")
    void update(Course course);

    @Delete("DELETE FROM course WHERE id = #{id}")
    void delete(Long id);
}
