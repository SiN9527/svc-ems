package com.svc.ems.config.jwt;

import com.svc.ems.entity.AdminMainEntity;
import com.svc.ems.entity.AdminRoleEntity;

import com.svc.ems.dto.base.JwtUserDetails;
import com.svc.ems.repo.AdminMainRepository;
import com.svc.ems.repo.AdminMainRoleRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final AdminMainRepository adminMainRepository;
    private final AdminMainRoleRepository adminMainRoleRepository;

    // 使用構造器注入，並使用 final 保持依賴的不可變性
    public JwtUserDetailsService(AdminMainRepository adminMainRepository,
                                 AdminMainRoleRepository adminMainRoleRepository) {
        this.adminMainRepository = adminMainRepository;
        this.adminMainRoleRepository = adminMainRoleRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 查詢使用者
        AdminMainEntity user = adminMainRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not found with email"));
        if (!user.getEnabled()) {
            throw new UsernameNotFoundException("Account is disabled");
        }

        // 透過關聯表查詢該使用者的角色
        List<AdminRoleEntity> roles = adminMainRoleRepository.findRolesByUserId(user.getUserId());

        // 轉換成 Spring Security 需要的角色格式
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleCode())) // Spring Security 需要加 "ROLE_"
                .collect(Collectors.toList());

        // 返回 Spring Security 的 UserDetails
        return new JwtUserDetails(user, authorities);
    }

   public boolean userExists(String email) {
        return adminMainRepository.existsByEmail(email);
    }
}
