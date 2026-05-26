package com.github.takatoshi0418.security;

import java.util.Collection;
import java.util.Collections;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.takatoshi0418.entity.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginUser implements UserDetails {

    @Getter
    private final User user;

    /**
     * ユーザの権限を取得する
     * @return ユーザの権限
     */
    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getAuthority()));
    }

    /**
     * ユーザのパスワードを取得する
     * @return ユーザのパスワード
     */
    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    /**
     * ユーザのユーザ名を取得する
     * @return ユーザのユーザ名
     */
    @Override
    public String getUsername() {
        return user.getEmployeeNumber();
    }

    /**
     * アカウントの有効期限を確認する
     * @return アカウントが有効期限内である場合true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * アカウントがロックされていないか確認する
     * @return アカウントがロックされていない場合true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 資格情報の有効期限を確認する
     * @return 資格情報が有効期限内である場合true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * ユーザが有効であるか確認する
     * @return ユーザが有効である場合true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
