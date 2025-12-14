package com.studentmanager.mapper;

import com.studentmanager.entity.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    @Select("SELECT * FROM department WHERE id = #{id}")
    Department findById(Long id);

    @Select("SELECT * FROM department ORDER BY id ASC")
    List<Department> findAll();

    @Insert("INSERT INTO department(dept_code, dept_name) VALUES(#{deptCode}, #{deptName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Department department);

    @Update("UPDATE department SET dept_code = #{deptCode}, dept_name = #{deptName} WHERE id = #{id}")
    void update(Department department);

    @Delete("DELETE FROM department WHERE id = #{id}")
    void delete(Long id);
}
