package com.studentmanager.mapper;

import com.studentmanager.entity.Major;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MajorMapper {

    @Select("SELECT m.*, d.dept_name AS departmentName FROM major m " +
            "LEFT JOIN department d ON m.department_id = d.id WHERE m.id = #{id}")
    Major findById(Long id);

    @Select("SELECT m.*, d.dept_name AS departmentName FROM major m " +
            "LEFT JOIN department d ON m.department_id = d.id ORDER BY m.id ASC")
    List<Major> findAll();

    @Select("SELECT m.*, d.dept_name AS departmentName FROM major m " +
            "LEFT JOIN department d ON m.department_id = d.id WHERE m.department_id = #{departmentId} ORDER BY m.id ASC")
    List<Major> findByDepartmentId(Long departmentId);

    @Insert("INSERT INTO major(major_code, major_name, department_id) VALUES(#{majorCode}, #{majorName}, #{departmentId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Major major);

    @Update("UPDATE major SET major_code = #{majorCode}, major_name = #{majorName}, department_id = #{departmentId} WHERE id = #{id}")
    void update(Major major);

    @Delete("DELETE FROM major WHERE id = #{id}")
    void delete(Long id);
}
