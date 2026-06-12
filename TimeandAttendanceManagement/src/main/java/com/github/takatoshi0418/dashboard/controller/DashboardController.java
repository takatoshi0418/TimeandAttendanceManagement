package com.github.takatoshi0418.dashboard.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.takatoshi0418.attendance.service.AttendanceService;
import com.github.takatoshi0418.auth.security.LoginUser;
import com.github.takatoshi0418.dashboard.dto.DashboardView;

import lombok.RequiredArgsConstructor;

/**
 * DashboardControllerクラス
 * ダッシュボード画面を表示するためのコントローラークラス
 */
@Controller
@RequiredArgsConstructor
public class DashboardController {

    /** 勤怠サービス  */
    private final AttendanceService attendanceService;

    /**
     * ダッシュボード画面を表示する
     * @return ダッシュボード画面
     */
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal LoginUser loginUser, Model model) {
        if (loginUser == null) {
            return "redirect:/login";
        }

        DashboardView dashboardView = attendanceService.getLatestAttendance(loginUser.getUser());
        model.addAttribute(dashboardView);
        return "dashboard/index";
    }
}
