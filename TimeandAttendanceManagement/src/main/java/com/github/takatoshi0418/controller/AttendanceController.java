package com.github.takatoshi0418.controller;

import java.time.YearMonth;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.takatoshi0418.exception.attendance.IllegalAttendanceException;
import com.github.takatoshi0418.security.LoginUser;
import com.github.takatoshi0418.service.attendance.AttendancePolicy;
import com.github.takatoshi0418.service.attendance.AttendanceService;
import com.github.takatoshi0418.view.attendance.MonthlyAttendanceView;

import lombok.RequiredArgsConstructor;

/**
 * 勤怠操作に関するコントローラークラス
 */
@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    /** 勤怠サービス */
    private final AttendanceService attendanceService;

    /**
     * 出勤操作
     * 
     * @param loginUser ログインユーザ情報
     * @return 勤怠画面にリダイレクト
     * @throws IllegalAttendanceException 不正な勤怠操作が行われた場合
     */
    @PostMapping("/clock-in")
    public String clockIn(@AuthenticationPrincipal LoginUser loginUser) throws IllegalAttendanceException {
        // ログインユーザが存在しない場合はログインページにリダイレクト
        if (loginUser == null) {
            return "redirect:/login";
        }
        attendanceService.clockIn(loginUser.getUser());
        return "redirect:/dashboard";
    }

    /**
     * 退勤操作
     * 
     * @param loginUser ログインユーザ情報
     * @return 勤怠画面にリダイレクト
     * @throws IllegalAttendanceException 不正な勤怠操作が行われた場合
     */
    @PostMapping("/clock-out")
    public String clockOut(@AuthenticationPrincipal LoginUser loginUser) throws IllegalAttendanceException {
        // ログインユーザが存在しない場合はログインページにリダイレクト
        if (loginUser == null) {
            return "redirect:/login";
        }
        attendanceService.clockOut(loginUser.getUser());
        return "redirect:/dashboard";
    }

    @GetMapping("/attendance-report")
    public String attendanceReport(@AuthenticationPrincipal LoginUser loginUser, Model model)
            throws IllegalAttendanceException {
        YearMonth yearMonth = YearMonth.now();
        AttendancePolicy policy = AttendancePolicy.from();
        MonthlyAttendanceView monthlyAttendanceView = attendanceService.getMonthlyAttendanceView(loginUser.getUser(),
                yearMonth, policy);
        model.addAttribute("monthlyAttendanceView",monthlyAttendanceView);
        return "attendance/monthlyAttendanceReport";
    }
}
