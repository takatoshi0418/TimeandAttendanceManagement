package com.github.takatoshi0418.dashboard.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.github.takatoshi0418.attendance.domain.Attendance;

/** 今日の出勤・退勤情報 */
public record TodayAttendanceView(LocalDateTime clockInTime, LocalDateTime clockOutTime) {

    public static final String EMPTY_TIME_TEXT = "--:--";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm", Locale.JAPAN);


    /**
     * 出勤打刻が存在するかどうかを返す
     * @return true:出勤打刻あり / false:出勤打刻なし
     */
    public boolean isClockedIn() {
        return this.clockInTime != null;
    }

    /**
     * 出勤打刻が可能かどうかを返す
     * @return true:出勤打刻可能 / false:出勤打刻不可
     */
    public boolean isEnableClockIn() {
        return this.clockInTime == null;
    }

    /**
     * 退勤打刻が可能かどうかを返す
     * @return true:退勤打刻可能 / false:退勤打刻不可
     */
    public boolean isEnableClockOut() {
        return this.clockInTime != null && this.clockOutTime == null;
    }

    /**
     * 退勤打刻が存在するかどうかを返す
     * @return true:退勤打刻あり / false:退勤打刻なし
     */
    public boolean isClockedOut() {
        return this.clockOutTime != null;
    }

    /**
     * 出勤時間を取得する
     * @return 出勤時間 / 未打刻の場合は--:--を返す
     */
    public String formatedClockInTime() {
        return isClockedIn() ? this.clockInTime.format(FORMATTER) : EMPTY_TIME_TEXT;
    }

    /**
     * 退勤時間を取得する
     * @return 退勤時間 / 未打刻の場合は--:--を返す
     */
    public String formatedClockOutTime() {
        return isClockedOut() ? this.clockOutTime.format(FORMATTER) : EMPTY_TIME_TEXT;
    }

    /**
     * 勤務エンティティから出勤・退勤情報を作成する
     * @param attendance 勤務エンティティ
     * @return 出勤・退勤情報
     */
    public static TodayAttendanceView from(Attendance attendance) {
        if (attendance == null) {
            return new TodayAttendanceView(null, null);
        } else {
            return new TodayAttendanceView(attendance.getClockIn(), attendance.getClockOut());
        }
    }

}
