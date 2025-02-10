package com.svc.ems.config.jwt;

import com.svc.ems.dto.base.JwtMemberDetails;
import com.svc.ems.entity.MemberMain;
import com.svc.ems.entity.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.svc.ems.repo.MemberMainRepository;
import com.svc.ems.repo.MemberMainRoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtMemberDetailsService implements UserDetailsService {

    private final MemberMainRepository memberMainRepository;
    private final MemberMainRoleRepository memberMainRoleRepository;

    // 使用構造器注入
    public JwtMemberDetailsService(MemberMainRepository memberMainRepository,
                                   MemberMainRoleRepository memberMainRoleRepository) {
        this.memberMainRepository = memberMainRepository;
        this.memberMainRoleRepository = memberMainRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 查詢會員
        MemberMain member = memberMainRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with email: " + email));

        // 透過關聯表查詢該會員的角色
        List<MemberRole> roles = memberMainRoleRepository.findRolesByMemberId(member.getMemberId());

        // 轉換成 Spring Security 需要的角色格式
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleCode())) // Spring Security 需要加 "ROLE_"
                .collect(Collectors.toList());

        // 返回 Spring Security 的 UserDetails
        return new JwtMemberDetails(member, authorities);
    }
}
