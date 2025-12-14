package com.studentmanager.mapper;

import com.studentmanager.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeacherMapper {

    @Select("SELECT * FROM teacher WHERE id = #{id}")
    Teacher findById(Long id);

    @Select("SELECT * FROM teacher WHERE teacher_no = #{teacherNo}")
    Teacher findByTeacherNo(String teacherNo);

    @Select("SELECT * FROM teacher ORDER BY id LIMIT #{offset}, #{limit}")
    List<Teacher> findAll(@Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO teacher(teacher_no, name, phone, department) VALUES(#{teacherNo}, #{name}, #{phone}, #{department})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Teacher teacher);

    @Update("UPDATE teacher SET name=#{name}, phone=#{phone}, department=#{department} WHERE id=#{id}")
    void update(Teacher teacher);

    @Delete("DELETE FROM teacher WHERE id = #{id}")
    void delete(Long id);
}
