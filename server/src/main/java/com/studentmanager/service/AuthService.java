package com.studentmanager.service;

import com.studentmanager.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(String username, String password);

    void resetPassword(String username, String newPassword);
}
