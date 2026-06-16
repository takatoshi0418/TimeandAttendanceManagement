package com.github.takatoshi0418.attendance.domain.calculator;

import java.time.LocalDate;

import com.github.takatoshi0418.attendance.domain.WorkTimeResult;
import com.github.takatoshi0418.attendance.domain.entity.Attendance;
import com.github.takatoshi0418.attendance.domain.policy.AttendancePolicy;

/**
 * 勤務形態の計算を行うクラス
 */
public interface AttendanceCalculator {

    /**
     * 勤務形態の計算を行い、その結果を返す
     * @param targetDate 対象日
     * @param attendance 勤怠情報
     * @param policy 勤怠ポリシー
     * @return 勤怠計算の結果オブジェクト
     */
    WorkTimeResult calculate(LocalDate targetDate, Attendance attendance, AttendancePolicy policy);
}
