package com.svc.ems.dto.base;

import com.svc.ems.entity.MemberMainEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class JwtMemberDetails implements UserDetails {

    private final MemberMainEntity member;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtMemberDetails(MemberMainEntity member, Collection<? extends GrantedAuthority> authorities) {
        this.member = member;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword(); // 返回密碼
    }

    @Override
    public String getUsername() {
        return member.getEmail(); // 返回 email 作為用戶名
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

    @Override
    public boolean isEnabled() {
        return member.getEnabled(); // 返回會員的啟用狀態
    }


}