package com.github.takatoshi0418.attendance.domain.calculator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import com.github.takatoshi0418.attendance.domain.LocalDateTimeRange;
import com.github.takatoshi0418.attendance.domain.WorkTimeResult;
import com.github.takatoshi0418.attendance.domain.entity.Attendance;
import com.github.takatoshi0418.attendance.domain.policy.AttendancePolicy;

/**
 * 通常勤務形態の計算
 */
public class StandardAttendanceCalcurator implements AttendanceCalculator {

    /**
     * 勤務形態の計算を行い、その結果を返す
     * @param targetDate 対象日
     * @param attendance 勤怠情報
     * @param policy 勤怠ポリシー
     * @return 勤怠計算の結果オブジェクト
     */
    @Override
    public WorkTimeResult calculate(LocalDate targetDate, Attendance attendance, AttendancePolicy policy) {

        // 勤務が存在しない場合、休みとみなす
        if (attendance == null) {
            return WorkTimeResult.zero(targetDate);
        }
        LocalTime clockInTime = attendance.getClockIn().toLocalTime();
        LocalTime clockOutTime = attendance.getClockOut().toLocalTime();

        LocalDateTimeRange workTimeRange = new LocalDateTimeRange(clockInTime, clockOutTime);

        long diffTimeMinutes = getDiffTimeMinutes(workTimeRange, policy.policyWorkTimeRange());
        return new WorkTimeResult(
            targetDate,
            clockInTime,
            clockOutTime,
            getActualWorkTimeMinutes(workTimeRange, policy.breakTimeRanges()),
            getOvertimeMinutes(diffTimeMinutes),
            getDeductedTimeMinutes(diffTimeMinutes),
            getLateNightTimeMinutes(workTimeRange, policy.policyLateNightRange()),
            getHolidayTimeMinutes(targetDate, workTimeRange)
        );
    }

    /**
     * 標準労働時間との差分（分）を取得する
     * @param workTimeRange 勤務時間範囲
     * @param policyWorkTimeRange 標準労働時間範囲
     * @return 標準労働時間との差分（分）
     */
    private long getDiffTimeMinutes(LocalDateTimeRange workTimeRange, LocalDateTimeRange policyWorkTimeRange) {
        return workTimeRange.getBetweenMinutes() - policyWorkTimeRange.getBetweenMinutes();
    }

    /**
     * 時間外労働時間（分）を取得する
     * @param diffTimeMinutes 標準労働時間との差分
     * @return 時間外労働時間（分）
     */
    private long getOvertimeMinutes(long diffTimeMinutes) {
        return Math.max(0, diffTimeMinutes);
    }

    /**
     * 控除時間（分）を取得する
     * @param diffTimeMinutes 標準労働時間との差分
     * @return 控除時間（分）
     */
    private long getDeductedTimeMinutes(long diffTimeMinutes) {
        return diffTimeMinutes < 0 ? Math.abs(diffTimeMinutes) : 0;
    }

    /**
     * 休憩を除いた実働時間（分）を取得する
     * @param workTimeRange 勤務時間範囲
     * @param breakTimeRanges 休憩時間範囲の配列
     * @return 休憩を除いた実働時間（分）
     */
    private long getActualWorkTimeMinutes(LocalDateTimeRange workTimeRange, LocalDateTimeRange[] breakTimeRanges) {
        long breakTimeMinutes = 0;
        for (LocalDateTimeRange breakTimeRange : breakTimeRanges) {
            if (workTimeRange.isBetween(breakTimeRange.getStartTime())) {
                breakTimeMinutes += breakTimeRange.overlapTimeMinutes(workTimeRange);
            }
        }
        return workTimeRange.getBetweenMinutes() - breakTimeMinutes;
    }

    /**
     * 深夜勤務時間を取得する
     * @param workTimeRange 勤務時間範囲
     * @param policyLateNightRange 深夜勤務時間の範囲
     * @return 深夜勤務時間
     */
    private long getLateNightTimeMinutes(LocalDateTimeRange workTimeRange, LocalDateTimeRange policyLateNightRange) {
        return workTimeRange.overlapTimeMinutes(policyLateNightRange);
    }

    /**
     * 休日勤務時間を取得する
     * @param clockInDate 出勤日
     * @param workTimeRange 勤務時間範囲
     * @return 休日勤務時間
     */
    private long getHolidayTimeMinutes(LocalDate clockInDate, LocalDateTimeRange workTimeRange) {
        if (DayOfWeek.SUNDAY.equals(clockInDate.getDayOfWeek())) {
            return workTimeRange.overlapTimeMinutes(new LocalDateTimeRange(LocalTime.MIN, LocalTime.MAX));
        }
        return 0;
    }
}
