package com.studentmanager.mapper;

import com.studentmanager.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    @Select("SELECT * FROM student WHERE id = #{id}")
    Student findById(Long id);

    @Select("SELECT * FROM student WHERE student_no = #{studentNo}")
    Student findByStudentNo(String studentNo);

        @Select("SELECT * FROM student ORDER BY id LIMIT #{offset}, #{limit}")
        List<Student> findAll(@Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO student(student_no, name, age, gender, phone, department, major, class_id) " +
            "VALUES(#{studentNo}, #{name}, #{age}, #{gender}, #{phone}, #{department}, #{major}, #{classId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Student student);

    @Update("UPDATE student SET name=#{name}, age=#{age}, gender=#{gender}, phone=#{phone}, " +
            "department=#{department}, major=#{major}, class_id=#{classId} WHERE id=#{id}")
    void update(Student student);

    @Delete("DELETE FROM student WHERE id = #{id}")
    void delete(Long id);
}
