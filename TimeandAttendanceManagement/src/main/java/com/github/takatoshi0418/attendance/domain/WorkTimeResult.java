package com.github.takatoshi0418.attendance.domain;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 勤務形態の計算結果を管理する
 */
public record WorkTimeResult(
    /** 対象日時 */
    LocalDate targetDate,
    /** 出勤時間 */
    LocalTime clockInTime,
    /** 退勤時間 */
    LocalTime clockOutTime,
    /** 勤務時間（分） */
    long workTimeMinutes,
    /** 時間外労働時間（分） */
    long overtimeMinutes,
    /** 控除時間（分） */
    long deductedTimeMinutes,
    /** 深夜勤務時間（分） */
    long lateNightTimeMinutes,
    /** 休日勤務時間（分） */
    long holidayTimeMinutes
) {
    /**
     * 勤務がない日の結果を取得する
     * @param targetDate 対象日
     * @return 勤務がない日の計算結果
     */
    public static WorkTimeResult zero(LocalDate targetDate) {
        return new WorkTimeResult(targetDate, null, null, 0, 0, 0, 0, 0);
    }
}
