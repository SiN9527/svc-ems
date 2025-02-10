package com.svc.ems.dto.base;

import com.svc.ems.entity.UserMain;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class JwtUserDetails implements UserDetails {

    private final UserMain userMain;
    private final List<GrantedAuthority> authorities; // 新增角色清單

    public JwtUserDetails(UserMain userMain, List<GrantedAuthority> authorities) {
        this.userMain = userMain;
        this.authorities = authorities; // 接收角色權限
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // 直接回傳角色權限
    }

    @Override
    public String getPassword() {
        return userMain.getPassword();
    }

    @Override
    public String getUsername() {
        return userMain.getUserName();
    }


    @Override
    public boolean isEnabled() {
        return userMain.getEnabled();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getEmail() {
        return userMain.getEmail();
    }


    public UserMain getUser() {
        return userMain;
    }
}
