package com.github.takatoshi0418.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.takatoshi0418.security.LoginUser;
import com.github.takatoshi0418.service.AttendanceService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

private final AttendanceService attendanceService;

    @PostMapping("/clock-in")
    public String clockIn(@AuthenticationPrincipal LoginUser loginUser) {
        // ログインユーザが存在しない場合はログインページにリダイレクト
        if (loginUser == null) {
            return "redirect:/login";
        }
        attendanceService.clockIn(loginUser.getUser());
        return "redirect:/dashboard";
    }

    @PostMapping("/clock-out")
    public String clockOut(@AuthenticationPrincipal LoginUser loginUser) {
        // ログインユーザが存在しない場合はログインページにリダイレクト
        if (loginUser == null) {
            return "redirect:/login";
        }
        attendanceService.clockOut(loginUser.getUser());
        return "redirect:/dashboard";
    }
}
