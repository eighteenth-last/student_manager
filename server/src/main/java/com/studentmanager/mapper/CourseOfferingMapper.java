package com.studentmanager.mapper;

import com.studentmanager.entity.CourseOffering;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseOfferingMapper {

    @Select("SELECT co.*, t.name as teacher_name " +
            "FROM course_offering co " +
            "LEFT JOIN teacher t ON co.teacher_id = t.id " +
            "WHERE co.id = #{id}")
    CourseOffering findById(Long id);

    @Select("SELECT co.*, t.name as teacher_name " +
            "FROM course_offering co " +
            "LEFT JOIN teacher t ON co.teacher_id = t.id " +
            "WHERE co.semester = #{semester} AND co.status = #{status} " +
            "ORDER BY co.id LIMIT #{offset}, #{limit}")
    List<CourseOffering> findBySemesterAndStatus(@Param("semester") String semester, 
                                                   @Param("status") String status,
                                                   @Param("offset") int offset, 
                                                   @Param("limit") int limit);

    @Select("SELECT co.*, t.name as teacher_name " +
            "FROM course_offering co " +
            "LEFT JOIN teacher t ON co.teacher_id = t.id " +
            "WHERE co.status = 'OPEN' " +
            "ORDER BY co.semester DESC, co.id")
    List<CourseOffering> findAllOpen();

    @Select("SELECT co.*, t.name as teacher_name " +
            "FROM course_offering co " +
            "LEFT JOIN teacher t ON co.teacher_id = t.id " +
            "WHERE co.semester = #{semester} " +
            "ORDER BY co.id")
    List<CourseOffering> findBySemester(@Param("semester") String semester);

    @Select("SELECT co.*, t.name as teacher_name " +
            "FROM course_offering co " +
            "LEFT JOIN teacher t ON co.teacher_id = t.id " +
            "WHERE co.teacher_id = #{teacherId} " +
            "ORDER BY co.semester DESC, co.id")
    List<CourseOffering> findByTeacherId(@Param("teacherId") Long teacherId);

    @Select("SELECT co.*, t.name as teacher_name " +
            "FROM course_offering co " +
            "LEFT JOIN teacher t ON co.teacher_id = t.id " +
            "ORDER BY co.semester DESC, co.id LIMIT #{offset}, #{limit}")
    List<CourseOffering> findAll(@Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO course_offering(course_no, course_name, credit, semester, teacher_id, class_time, classroom, max_students, current_students, status) " +
            "VALUES(#{courseNo}, #{courseName}, #{credit}, #{semester}, #{teacherId}, #{classTime}, #{classroom}, #{maxStudents}, #{currentStudents}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(CourseOffering offering);

    @Update("UPDATE course_offering SET course_no=#{courseNo}, course_name=#{courseName}, credit=#{credit}, semester=#{semester}, teacher_id=#{teacherId}, " +
            "class_time=#{classTime}, classroom=#{classroom}, max_students=#{maxStudents}, " +
            "current_students=#{currentStudents}, status=#{status} WHERE id=#{id}")
    void update(CourseOffering offering);

    @Update("UPDATE course_offering SET current_students = current_students + 1 WHERE id=#{id}")
    void incrementCurrentStudents(Long id);

    @Update("UPDATE course_offering SET current_students = current_students - 1 WHERE id=#{id} AND current_students > 0")
    void decrementCurrentStudents(Long id);

    @Delete("DELETE FROM course_offering WHERE id = #{id}")
    void deleteById(Long id);
}
