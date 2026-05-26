package com.github.takatoshi0418.service;

import com.github.takatoshi0418.entity.User;

/**
 * 出勤・退勤処理を行うサービスインターフェース
 */
public interface AttendanceService {

    /**
     * ユーザの出勤処理を行う
     * @param user 出勤するユーザ
     */
    void clockIn(User user);

    /**
     * ユーザの退勤処理を行う
     * @param user 退勤するユーザ
     */
    void clockOut(User user);

    /**
     * ユーザが現在出勤中かどうかを判定する
     * @param user 判定するユーザ
     * @return 出勤中であればtrue、そうでなければfalse
     */
    boolean isClockedIn(User user);
}
