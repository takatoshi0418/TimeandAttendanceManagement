package com.github.takatoshi0418.service;

import java.util.Optional;

import com.github.takatoshi0418.entity.Attendance;
import com.github.takatoshi0418.entity.User;
import com.github.takatoshi0418.exception.attendance.IllegalAttendanceException;

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
    Optional<Attendance> getLatestAttendance(User user);
}
