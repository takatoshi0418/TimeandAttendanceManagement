package com.github.takatoshi0418.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * DashboardControllerクラス
 * ダッシュボード画面を表示するためのコントローラークラス
 */
@Controller
public class DashboardController {

    /**
     * ダッシュボード画面を表示する
     * @return ダッシュボード画面
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/index";
    }
}
