package com.peachdevops.community.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table
@Getter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    protected Long id;

    @Setter
    @Column(nullable = false)
    protected String username;

    @Setter
    @Column(nullable = false)
    protected String password;

    @Setter
    @Column(nullable = false)
    protected String nickname;

    @Setter
    @Column(nullable = false)
    protected String authority;

    @Setter
    @Column(nullable = false)
    protected boolean isEmailVerifiedFlag;

    @Setter
    @Column(nullable = false)
    protected boolean isSuspendedFlag;

    @Setter
    @Column(nullable = false)
    protected boolean isDeletedFlag;

    @Setter
    @Column(nullable = false)
    protected boolean isStdCardVerifiedFlag;

    @CreatedDate
    @Column(insertable = false, updatable = false)
    protected final LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(insertable = false, updatable = false)
    protected final LocalDateTime updatedAt = LocalDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> authority);
    }

    public User(String username, String password, String nickname,String authority) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.authority = authority;
    }

    public User() {
    }

    public Boolean isAdmin() {
        return authority.equals("ROLE_ADMIN");
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
        return true;
    }
}
