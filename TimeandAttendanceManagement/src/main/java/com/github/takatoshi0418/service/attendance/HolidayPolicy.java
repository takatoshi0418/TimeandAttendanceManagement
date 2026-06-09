package com.github.takatoshi0418.service.attendance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.temporal.WeekFields;
import java.util.Locale;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 休日の法則を定義するクラス
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HolidayPolicy {
    private final Month month;
    private final int day;
    private final DayOfWeek dayOfWeek;
    private final int weekNum;
    private final LocalDate startDate;
    private final LocalDate endDate;

    /**
     * コンストラクタ
     * 休日を決める法則が曜日だけの場合
     * 
     * @param dayOfWeek 休日の曜日
     */
    public HolidayPolicy(DayOfWeek dayOfWeek) {
        this(null, -1, dayOfWeek, -1, LocalDate.MIN, LocalDate.MAX);
    }

    /**
     * コンストラクタ
     * 休日を決める法則が月日の場合
     * @param month 月
     * @param day 日
     */
    public HolidayPolicy(Month month, int day) {
        this(month, day, null, -1, LocalDate.MIN, LocalDate.MAX);
    }

    /**
     * コンストラクタ
     * 休日を決める法則が月の第何曜日のような場合
     * @param month 月
     * @param dayOfWeek 曜日
     * @param weekNum 週の回数
     */
    public HolidayPolicy(Month month, DayOfWeek dayOfWeek, int weekNum) {
        this(month, -1, dayOfWeek, weekNum, null, null);
    }

    /**
     * 指定した日が休日の法則に当てはまるかどうかを返す
     * @param targetDate 休日かどうかを判断する日
     * @return true: 当てはまる場合 | false: 当てはまらない場合
     */
    public boolean isHoliday(LocalDate targetDate) {
        if (targetDate == null) {
            return false;
        }
        if (startDate.isBefore(targetDate) || endDate.isAfter(targetDate)) {
            return false;
        }
        if (month != null && day != -1) {
            return MonthDay.of(month, day).equals(MonthDay.from(targetDate));
        } else if (dayOfWeek != null && weekNum == -1) {
            return dayOfWeek.equals(targetDate.getDayOfWeek());
        } else if (month != null && dayOfWeek != null && weekNum != -1) {
            return
                month.equals(targetDate.getMonth()) &&
                dayOfWeek.equals(targetDate.getDayOfWeek()) &&
                weekNum == targetDate.get(WeekFields.of(Locale.JAPAN).weekOfMonth());
        }
        return false;
    }
}
