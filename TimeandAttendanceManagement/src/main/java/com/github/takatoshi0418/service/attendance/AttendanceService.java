package com.github.takatoshi0418.service.attendance;

import java.time.YearMonth;

import com.github.takatoshi0418.exception.attendance.IllegalAttendanceException;
import com.github.takatoshi0418.model.AttendancePolicy;
import com.github.takatoshi0418.model.entity.User;
import com.github.takatoshi0418.model.view.attendance.MonthlyAttendanceView;
import com.github.takatoshi0418.model.view.dashboard.DashboardView;

/**
 * 出勤・退勤処理を行うサービスインターフェース
 */
public interface AttendanceService {

    /**
     * ユーザの出勤処理を行う
     * @param user 出勤するユーザ
     */
    void clockIn(User user) throws IllegalAttendanceException;

    /**
     * ユーザの退勤処理を行う
     * @param user 退勤するユーザ
     */
    void clockOut(User user) throws IllegalAttendanceException;

    /**
     * ユーザが現在出勤中かどうかを判定する
     * @param user 判定するユーザ
     * @return 出勤中であればtrue、そうでなければfalse
     */
    boolean isClockedIn(User user);

    /**
     * 最新の出勤情報を取得する
     * @param user 最新の出勤情報を取得したいユーザ
     * @return 最新の出勤情報、存在しない場合は空のOptional
     */
    DashboardView getLatestAttendance(User user);

    /**
     * ユーザの月別勤怠情報を取得する
     * @param user 月別勤怠を取得したいユーザ
     * @param targeYearMonth 月別勤怠を取得したい年月
     * @param policy 勤怠ポリシー
     * @return ユーザの月別勤怠情報、存在しない場合は空
     */
    MonthlyAttendanceView getMonthlyAttendanceView(User user, YearMonth targeYearMonth, AttendancePolicy policy);
}
