package com.github.takatoshi0418.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.takatoshi0418.entity.Attendance;
import com.github.takatoshi0418.security.LoginUser;
import com.github.takatoshi0418.service.attendance.AttendanceService;
import com.github.takatoshi0418.view.dashboard.DashboardView;
import com.github.takatoshi0418.view.dashboard.TodayAttendanceView;

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

        // TODO 詰め替え処理をService層に移動
        Attendance attendance = attendanceService.getLatestAttendance(loginUser.getUser()).orElse(null);
        TodayAttendanceView todayAttendanceView = TodayAttendanceView.from(attendance);
        DashboardView dashboardView = new DashboardView(todayAttendanceView);
        model.addAttribute(dashboardView);
        return "dashboard/index";
    }
}
