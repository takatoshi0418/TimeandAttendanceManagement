package com.github.takatoshi0418.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * LoginControllerクラス
 * ログイン画面を表示するためのコントローラークラス
 */
@Controller
public class LoginController {

    /** ログイン画面を表示する
     * @return ログイン画面
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
