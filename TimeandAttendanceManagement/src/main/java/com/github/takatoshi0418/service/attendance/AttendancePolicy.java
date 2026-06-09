package com.github.takatoshi0418.service.attendance;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.Month;

import com.github.takatoshi0418.service.LocalDateTimeRange;

/**
 * 勤怠に関するポリシー
 */
public record AttendancePolicy(
    /** 勤務範囲 */
    LocalDateTimeRange policyWorkTimeRange,
    /** 夜間勤務範囲 */
    LocalDateTimeRange policyLateNightRange,
    /** 休憩時間範囲の配列（複数ある場合を考慮） */
    LocalDateTimeRange[] breakTimeRanges,
    /** 休日のポリシー */
    HolidayPolicy[] holidayPolicies
) {

    /**
     * 定義された勤務ポリシーを生成
     * @return 定義された勤怠ポリシーを返却
     */
    public static AttendancePolicy from() {
        // 勤務時間範囲を取得
        LocalDateTimeRange policyWorkTimeRange = new LocalDateTimeRange(
                LocalTime.of(9,30),
                LocalTime.of(18,30)
        );
        // 深夜時間範囲を取得
        LocalDateTimeRange policyLateNightRange = new LocalDateTimeRange(
                LocalTime.of(22,0),
                LocalTime.of(5,0)
        );
        // 休憩時間範囲の配列を取得
        LocalDateTimeRange[] breakTimeRanges = new LocalDateTimeRange[]{
            new LocalDateTimeRange(
                LocalTime.of(12,00), 
                LocalTime.of(13,00)
            )
        };
        // 休日を取得
        HolidayPolicy[] holidayPolicies = new HolidayPolicy[]{
            new HolidayPolicy(DayOfWeek.SATURDAY),
            new HolidayPolicy(DayOfWeek.SUNDAY),
            new HolidayPolicy(Month.JANUARY, 1),
            new HolidayPolicy(Month.JANUARY, 2),
            new HolidayPolicy(Month.JANUARY, 3),
            new HolidayPolicy(Month.JANUARY, DayOfWeek.MONDAY, 2)
        };
        return new AttendancePolicy(policyWorkTimeRange, policyLateNightRange, breakTimeRanges, holidayPolicies);
    }
}
