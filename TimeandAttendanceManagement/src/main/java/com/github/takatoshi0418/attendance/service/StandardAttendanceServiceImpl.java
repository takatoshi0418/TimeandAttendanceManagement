package com.github.takatoshi0418.attendance.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.takatoshi0418.attendance.domain.Attendance;
import com.github.takatoshi0418.attendance.domain.AttendancePolicy;
import com.github.takatoshi0418.attendance.domain.LocalDateTimeRange;
import com.github.takatoshi0418.attendance.dto.DailyAttendanceView;
import com.github.takatoshi0418.attendance.dto.MonthlyAttendanceView;
import com.github.takatoshi0418.attendance.exception.AlreadyClockedInException;
import com.github.takatoshi0418.attendance.exception.AttendanceNotFoundException;
import com.github.takatoshi0418.attendance.exception.IllegalAttendanceException;
import com.github.takatoshi0418.attendance.exception.MultipleClockInRecordsException;
import com.github.takatoshi0418.attendance.repository.AttendanceRepository;
import com.github.takatoshi0418.auth.dto.User;
import com.github.takatoshi0418.dashboard.dto.DashboardView;
import com.github.takatoshi0418.dashboard.dto.TodayAttendanceView;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * StandardAttendanceServiceImplクラス
 * 固定労働を実装するAttendanceServiceの実装クラス
 */
@Service
@RequiredArgsConstructor
public class StandardAttendanceServiceImpl implements AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(StandardAttendanceServiceImpl.class);

    /**
     * AttendanceRepositoryを注入
     * 勤務記録を管理するためのリポジトリ
     */
    private final AttendanceRepository attendanceRepository;

    /**
     * ユーザの出勤処理を行う
     * 
     * @param user 出勤するユーザ
     * @throws IllegalAttendanceException 既に出勤記録が存在する場合
     */
    @Override
    @Transactional
    public void clockIn(@NonNull User user) throws IllegalAttendanceException {

        List<Attendance> attendances = attendanceRepository.findByUserIdAndClockOutIsNull(user.getId());
        if (attendances.size() > 0) {
            throw new AlreadyClockedInException(user.getId());
        }
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setClockIn(LocalDateTime.now());
        attendanceRepository.save(attendance);

        logger.info("{}さんが出勤しました。", user.getFullName());
    }

    /**
     * ユーザの退勤処理を行う
     * 
     * @param user 退勤するユーザ
     * @throws IllegalAttendanceException 退勤打刻が存在しない場合、または既に退勤している場合
     */
    @Override
    @Transactional
    public void clockOut(@NonNull User user) throws IllegalAttendanceException {

        List<Attendance> attendances = attendanceRepository.findByUserIdAndClockOutIsNull(user.getId());
        if (attendances.isEmpty()) {
            throw new AttendanceNotFoundException(user.getId());
        } else if (attendances.size() > 1) {
            throw new MultipleClockInRecordsException(user.getId());
        } else {
            Attendance attendance = attendances.get(0);
            attendance.setUser(user);
            attendance.setClockOut(LocalDateTime.now());
            attendanceRepository.save(attendance);
        }

        logger.info("{}さんが退勤しました。", user.getFullName());
    }

    /**
     * ユーザが出勤中かどうかを確認する
     * 
     * @param user 確認するユーザ
     * @return 出勤中の場合はtrue、それ以外はfalse
     */
    @Override
    public boolean isClockedIn(@NonNull User user) {
        return attendanceRepository.findByUserIdAndClockOutIsNull(user.getId()).size() > 0;
    }

    /**
     * 最新の出勤情報を取得する
     * 
     * @param user 取得したいユーザ
     * @return 最新の出勤情報、存在しない場合は空のOptional
     */
    public DashboardView getLatestAttendance(@NonNull User user) {
        Attendance attendance = attendanceRepository.findTopByUserIdAndClockOutIsNullOrderByClockInDesc(user.getId())
                .orElse(null);
        TodayAttendanceView todayAttendanceView = TodayAttendanceView.from(attendance);
        return new DashboardView(todayAttendanceView);
    }

    /**
     * ユーザの月別勤怠情報を取得する
     * 
     * @param user           月別勤怠を取得したいユーザ
     * @param targeYearMonth 月別勤怠を取得したい年月
     * @param policy         勤怠ポリシー
     * @return ユーザの月別勤怠情報、存在しない場合は空
     */
    public MonthlyAttendanceView getMonthlyAttendanceView(User user, YearMonth targeYearMonth,
            AttendancePolicy policy) {
        LocalDateTime startDateTime = LocalDateTime.of(targeYearMonth.atDay(1), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(targeYearMonth.atEndOfMonth(), LocalTime.MAX);
        List<Attendance> attendances = attendanceRepository.findByUserIdAndClockInBetweenOrderByClockInAsc(user.getId(),
                startDateTime, endDateTime);

        List<DailyAttendanceView> dailyAttendanceViews = new ArrayList<>();
        LocalDate currentDate = startDateTime.toLocalDate();
        LocalDate nextMonthFirstDay = endDateTime.toLocalDate().plusDays(1);
        while (currentDate.isBefore(nextMonthFirstDay)) {
            final LocalDate paramedCurrentDate = currentDate;
            // currentDateの勤務情報を取得して、日別勤怠情報を生成する
            Optional<Attendance> currentAttendance = attendances.stream()
                    .filter(attendace -> paramedCurrentDate.equals(attendace.getClockIn().toLocalDate()))
                    .findFirst();
            currentAttendance.ifPresentOrElse(attendance -> {
                dailyAttendanceViews.add(createAttendanceView(paramedCurrentDate, attendance, policy));
            }, () -> {
                dailyAttendanceViews.add(createAttendanceView(paramedCurrentDate, null, policy));
            });
            currentDate = currentDate.plusDays(1);
        }
        return MonthlyAttendanceView.from(targeYearMonth, dailyAttendanceViews);
    }

    private DailyAttendanceView createAttendanceView(LocalDate localDate, Attendance attendance,
            AttendancePolicy policy) {

        if (attendance == null) {
            return DailyAttendanceView.from(localDate);
        }

        LocalDateTimeRange worktimeRange = new LocalDateTimeRange(attendance.getClockIn().toLocalTime(),
                attendance.getClockOut().toLocalTime());

        // 実労働時間を計算
        long actualWorktimeMinutes = worktimeRange.getBetweenMinutes();
        // 実働時間の差分を求めて、時間外と控除時間を計算
        long diffTimeMinutes = actualWorktimeMinutes - policy.policyWorkTimeRange().getBetweenMinutes();
        long overtimeMinutes = diffTimeMinutes > 0 ? diffTimeMinutes : 0;
        long deductedTimeMinutes = diffTimeMinutes < 0 ? Math.abs(diffTimeMinutes) : 0;

        // 休憩時間の計算
        long breakTimeMinutes = 0;
        LocalDateTimeRange[] breakTimeRanges = policy.breakTimeRanges();
        for (LocalDateTimeRange breakTimeRange : breakTimeRanges) {
            if (worktimeRange.isBetween(breakTimeRange.getStartDateTime())) {
                breakTimeMinutes += overlapTimeMinutes(worktimeRange, breakTimeRange);
            }
        }
        actualWorktimeMinutes -= breakTimeMinutes;

        // 深夜勤務時間を計算する
        long lateNightTimeMinutes = overlapTimeMinutes(worktimeRange, policy.policyLateNightRange());

        // 一旦、法定休日を日曜日に固定
        // TODO のちのち、法定休日を自動計算できるようにする
        long holidayTimeMinutes = 0;
        if (DayOfWeek.SUNDAY.equals(localDate.getDayOfWeek())) {
            LocalDateTime startDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endDateTime = LocalDateTime.of(localDate, LocalTime.MAX);
            holidayTimeMinutes += overlapTimeMinutes(worktimeRange, new LocalDateTimeRange(startDateTime, endDateTime));
        }

        // TODO 備考の生成
        String note = "";

        return new DailyAttendanceView(localDate, attendance.getClockIn().toLocalTime(),
                attendance.getClockOut().toLocalTime(), actualWorktimeMinutes, overtimeMinutes, deductedTimeMinutes,
                lateNightTimeMinutes, holidayTimeMinutes, note);
    }

    /**
     * 2つの時間帯が重複する時間（分）を取得する。
     * @param datetimeRange1 時間帯1
     * @param datetimeRange2 時間帯2
     * @return 時間帯重複する時間（分）
     */
    private long overlapTimeMinutes(LocalDateTimeRange datetimeRange1, LocalDateTimeRange datetimeRange2) {
        LocalDateTime startDateTime = datetimeRange1.getStartDateTime().isBefore(datetimeRange2.getStartDateTime())
                ? datetimeRange2.getStartDateTime()
                : datetimeRange1.getStartDateTime();
        LocalDateTime endDateTime = datetimeRange1.getEndDateTime().isAfter(datetimeRange2.getEndDateTime())
                ? datetimeRange2.getEndDateTime()
                : datetimeRange1.getEndDateTime();
        if (startDateTime.isAfter(endDateTime)) {
            return 0;
        }
        return Duration.between(startDateTime, endDateTime).toMinutes();
    }
}
