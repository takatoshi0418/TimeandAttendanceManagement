package com.github.takatoshi0418.view.attendance;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public record MonthlyAttendanceView(
        YearMonth targetYearMonth,
        DailyAttendanceView[] dailyAttendanceViews) {
    
    public static MonthlyAttendanceView from(YearMonth targetYearMonth,
            List<DailyAttendanceView> dailyAttendanceViews) {
        return new MonthlyAttendanceView(targetYearMonth,
                dailyAttendanceViews.toArray(new DailyAttendanceView[dailyAttendanceViews.size()]));
    }

    public String formatedYearMonth() {
        return targetYearMonth.format(DateTimeFormatter.ofPattern("yyyy年M月", Locale.JAPAN));
    }
}
