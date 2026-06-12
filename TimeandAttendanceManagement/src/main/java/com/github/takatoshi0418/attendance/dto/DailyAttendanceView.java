package com.github.takatoshi0418.attendance.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 1日分の勤務状況を表示するクラス
 */
public record DailyAttendanceView(
        LocalDate targetDate,
        LocalTime clockInTime,
        LocalTime clockOutTime,
        long worktimeMinutes,
        long overtimeMinutes,
        long deductedTimeMinutes,
        long lateNightTimeMinutes,
        long holidayTimeMinutes,
        String note) {

    /**
     * 1日分の勤務を作成する
     * @param targetDate 対象日
     * @param clockInTime 出勤時間
     * @param clockOutTime 退勤時間
     * @param worktimeMinutes 1日の勤務時間
     * @param overtimeMinutes 時間外時間
     * @param deductedTimeMinutes 控除時間
     * @param lateNightTimeMinutes 深夜勤務時間
     * @param holidayTimeMinutes 休日勤務時間
     * @param note 備考
     * @return 1日分の勤務
     */
    public static DailyAttendanceView from(LocalDate targetDate, LocalTime clockInTime, LocalTime clockOutTime,
            long worktimeMinutes, long overtimeMinutes, long deductedTimeMinutes, long lateNightTimeMinutes,
            long holidayTimeMinutes, String note) {
        return new DailyAttendanceView(targetDate, clockInTime, clockOutTime, worktimeMinutes, overtimeMinutes,
                deductedTimeMinutes, lateNightTimeMinutes, holidayTimeMinutes, note);
    }

    /**
     * 1日分の勤務を作成する
     * @param targetDate 対象日
     * @return 1日分の勤務
     */
    public static DailyAttendanceView from(LocalDate targetDate) {
        return new DailyAttendanceView(targetDate, null, null, 0, 0,
                0, 0, 0, "");
    }

    /**
     * フォーマットされた対象日
     * @return フォーマットされた対象日
     */
    public String formatedTargetDay() {
        return targetDate.format(DateTimeFormatter.ofPattern("d(E)", Locale.JAPAN));
    }

    /**
     * フォーマットされた出勤時間
     * @return フォーマットされた出勤時間
     */
    public String formatedClockInTime() {
        if (clockInTime == null) {
            return "";
        }
        return clockInTime.format(getClockFormat());
    }

    /**
     * フォーマットされた退勤時間
     * @return フォーマットされた退勤時間
     */
    public String formatedClockOutTime() {
        if (clockOutTime == null) {
            return "";
        }
        return clockOutTime.format(getClockFormat());
    }

    /**
     * フォーマットされた1日の勤務時間
     * @return フォーマットされた1日の勤務時間
     */
    public String formatedWorktime() {
        long hours = worktimeMinutes / 60;
        long minutes = worktimeMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    /**
     * フォーマットされた時間外時間
     * @return フォーマットされた時間外時間
     */
    public String formatedOvertime() {
        long hours = overtimeMinutes / 60;
        long minutes = overtimeMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    /**
     * フォーマットされた控除時間
     * @return フォーマットされた控除時間
     */
    public String formatedDeductedTime() {
        long hours = deductedTimeMinutes / 60;
        long minutes = deductedTimeMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    /**
     * フォーマットされた深夜勤務時間
     * @return フォーマットされた深夜勤務時間
     */
    public String formatedLateNightTime() {
        long hours = lateNightTimeMinutes / 60;
        long minutes = lateNightTimeMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    /**
     * フォーマットされた休日勤務時間
     * @return フォーマットされた休日勤務時間
     */
    public String formatedHolidayTime() {
        long hours = holidayTimeMinutes / 60;
        long minutes = holidayTimeMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    /**
     * レイアウトクラスを取得する
     * @return レイアウトクラス
     */
    public String GetLayoutClass() {
        if (DayOfWeek.SATURDAY.equals(targetDate.getDayOfWeek())) {
            return "saturday";
        } else if (DayOfWeek.SUNDAY.equals(targetDate.getDayOfWeek())) {
            return "sunday";
        }
        return "";
    }

    /**
     * 時分のフォーマッターを取得する
     * @return 時分のフォーマッター
     */
    private DateTimeFormatter getClockFormat() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }
}
