package com.studentmanager.mapper;

import com.studentmanager.entity.ClassInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClassInfoMapper {

    @Select("SELECT c.*, COALESCE(COUNT(s.id), 0) AS studentCount " +
            "FROM class_info c LEFT JOIN student s ON c.id = s.class_id " +
            "WHERE c.id = #{id} GROUP BY c.id")
    ClassInfo findById(Long id);

    @Select("SELECT * FROM class_info WHERE class_code = #{classCode}")
    ClassInfo findByClassCode(String classCode);

    @Select("SELECT c.*, COALESCE(COUNT(s.id), 0) AS studentCount " +
            "FROM class_info c LEFT JOIN student s ON c.id = s.class_id " +
            "GROUP BY c.id ORDER BY c.id LIMIT #{offset}, #{limit}")
    List<ClassInfo> findAll(@Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO class_info(class_code, major, head_teacher, entry_year) VALUES(#{classCode}, #{major}, #{headTeacher}, #{entryYear})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ClassInfo classInfo);

    @Update("UPDATE class_info SET major=#{major}, head_teacher=#{headTeacher}, entry_year=#{entryYear} WHERE id=#{id}")
    void update(ClassInfo classInfo);

    @Delete("DELETE FROM class_info WHERE id = #{id}")
    void delete(Long id);
}
