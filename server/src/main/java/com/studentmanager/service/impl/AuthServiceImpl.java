package com.studentmanager.service.impl;

import com.studentmanager.dto.LoginResponse;
import com.studentmanager.entity.SysUser;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.SysUserMapper;
import com.studentmanager.security.JwtUtil;
import com.studentmanager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(String username, String rawPassword) {
        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null || user.getStatus() == null || user.getStatus() == 0) {
            throw new BusinessException("账号不存在或已禁用");
        }
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new LoginResponse(token, user.getRole());
    }

    @Override
    public void resetPassword(String username, String newPassword) {
        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        String encoded = passwordEncoder.encode(newPassword);
        sysUserMapper.updatePassword(username, encoded);
    }
}
