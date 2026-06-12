package com.github.takatoshi0418.attendance.dto;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * 1か月分の勤務状況を表示するクラス
 */
public record MonthlyAttendanceView(
        YearMonth targetYearMonth,
        DailyAttendanceView[] dailyAttendanceViews) {
    
    /**
     * 1か月分の勤務状況を作成する
     * @param targetYearMonth 対象月
     * @param dailyAttendanceViews 対象月分の1日分の勤務状況の配列
     * @return 1か月分の勤務状況
     */
    public static MonthlyAttendanceView from(YearMonth targetYearMonth,
            List<DailyAttendanceView> dailyAttendanceViews) {
        return new MonthlyAttendanceView(targetYearMonth,
                dailyAttendanceViews.toArray(new DailyAttendanceView[dailyAttendanceViews.size()]));
    }

    /**
     * フォーマットされた対象月を取得する
     * @return フォーマットされた対象月
     */
    public String formatedYearMonth() {
        return targetYearMonth.format(DateTimeFormatter.ofPattern("yyyy年M月", Locale.JAPAN));
    }
}
