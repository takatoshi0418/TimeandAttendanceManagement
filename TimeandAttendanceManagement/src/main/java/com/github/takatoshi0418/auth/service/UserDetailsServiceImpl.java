package com.github.takatoshi0418.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.takatoshi0418.auth.dto.User;
import com.github.takatoshi0418.auth.repository.UserRepository;
import com.github.takatoshi0418.auth.security.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * UserDetailsServiceImplクラス
 * Spring SecurityのUserDetailsServiceを実装するクラス
 * ユーザの認証情報をデータベースから取得するためのサービスクラス
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    /**
     * UserRepositoryを注入
     * ユーザ情報をデータベースから取得するためのリポジトリ
     */
    private final UserRepository userRepository;

    /**
     * ユーザの認証情報をデータベースから取得するメソッド
     * @param employeeNumber 認証に使用する社員番号
     * @return LoginUser ログインユーザオブジェクト
     * @throws UsernameNotFoundException 指定された社員番号が見つからない場合にスローされる例外
     */
    @Override
    public LoginUser loadUserByUsername(String employeeNumber) throws UsernameNotFoundException {

        // DBから社員番号でユーザを検索
        User user = userRepository.findByEmployeeNumber(employeeNumber)
            .orElseThrow(() -> new UsernameNotFoundException("社員番号が見つかりません" + employeeNumber));


        logger.debug("--- 認証デバッグ開始 ---");
        logger.debug("入力ID:{}",employeeNumber);
        logger.debug("DB保存中のハッシュ値: {}", user.getPassword());
        logger.debug("ログインユーザー: {}", user.getLastName());
        logger.debug("権限情報: {}", user.getRole().getAuthority());
        logger.debug("--- 認証デバッグ終了 ---");

        // Spring SecurityのUserDetailsオブジェクトを返す
        return new LoginUser(user);
    }
}
