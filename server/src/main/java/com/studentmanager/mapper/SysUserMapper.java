package com.studentmanager.mapper;

import com.studentmanager.entity.SysUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SysUserMapper {

    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser findByUsername(String username);

    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    SysUser findById(Long id);

    @Insert("INSERT INTO sys_user(username, password, role, related_id, status) " +
            "VALUES(#{username}, #{password}, #{role}, #{relatedId}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(SysUser user);

    @Update("UPDATE sys_user SET password = #{password} WHERE username = #{username}")
    void updatePassword(@Param("username") String username, @Param("password") String password);
}
