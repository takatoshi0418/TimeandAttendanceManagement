package com.github.takatoshi0418.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * セキュリティ設定クラス
 * Spring Securityを使用して、アプリケーションのセキュリティ設定を行うためのクラス
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * セキュリティフィルタチェーンの定義
     * @param http HttpSecurityオブジェクトを受け取り、セキュリティ設定を行う
     * @return 	SecurityFilterChainオブジェクトを返す
     * @throws Exception セキュリティ設定に関する例外が発生する可能性があるため、Exceptionをスローする
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // h2-consoleを使用するための設定
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                .authorizeHttpRequests((auth) -> auth
                        // h2-consoleへのアクセスを全てのユーザに許可
                        .requestMatchers("/h2-console/**").permitAll()


                        // ログインページと静的リソースは全てのユーザに許可
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()

                        // /admin/** は ADMIN ロールを持つユーザのみアクセス可能
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        // その他のリクエストは認証が必要
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        // ログイン画面のURL
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout") // ログアウトURL
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());
        return http.build();
    }

    /**
     * パスワードエンコーダーの定義
     * @return BCryptPasswordEncoderオブジェクトを返す
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
