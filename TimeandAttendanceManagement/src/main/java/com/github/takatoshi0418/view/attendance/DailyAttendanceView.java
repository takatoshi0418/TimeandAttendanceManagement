package com.github.takatoshi0418.view.attendance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    public static DailyAttendanceView from(LocalDate targetDate, LocalTime clockInTime, LocalTime clockOutTime,
            long worktimeMinutes, long overtimeMinutes, long deductedTimeMinutes, long lateNightTimeMinutes,
            long holidayTimeMinutes, String note) {
        return new DailyAttendanceView(targetDate, clockInTime, clockOutTime, worktimeMinutes, overtimeMinutes,
                deductedTimeMinutes, lateNightTimeMinutes, holidayTimeMinutes, note);
    }

    public static DailyAttendanceView from(LocalDate targetDate) {
        return new DailyAttendanceView(targetDate, null, null, 0, 0,
                0, 0, 0, "");
    }

    public int targetDay() {
        return targetDate.getDayOfMonth();
    }

    public String formatedTargetDay() {
        return targetDate.format(DateTimeFormatter.ofPattern("d(E)", Locale.JAPAN));
    }

    public String formatedClockInTime() {
        if (clockInTime == null) {
            return "";
        }
        return clockInTime.format(getClockFormat());
    }

    public String formatedClockOutTime() {
        if (clockOutTime == null) {
            return "";
        }
        return clockOutTime.format(getClockFormat());
    }

    public String formatedWorktime() {
        long hours = worktimeMinutes / 60;
        long minutes = worktimeMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public String formatedOvertime() {
        long hours = overtimeMinutes / 60;
        long minutes = overtimeMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public String formatedDeductedTime() {
        long hours = deductedTimeMinutes / 60;
        long minutes = deductedTimeMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public String formatedLateNightTime() {
        long hours = lateNightTimeMinutes / 60;
        long minutes = lateNightTimeMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public String formatedHolidayTime() {
        long hours = holidayTimeMinutes / 60;
        long minutes = holidayTimeMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    private DateTimeFormatter getClockFormat() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }

    public String GetLayoutClass() {
        if (DayOfWeek.SATURDAY.equals(targetDate.getDayOfWeek())) {
            return "saturday";
        } else if (DayOfWeek.SUNDAY.equals(targetDate.getDayOfWeek())) {
            return "sunday";
        }
        return "";
    }
}
