package com.studentmanager.security;

import com.studentmanager.entity.SysUser;
import com.studentmanager.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
        boolean enabled = user.getStatus() == null || user.getStatus() == 1;
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of(authority))
                .disabled(!enabled)
                .build();
    }
}
