package com.github.takatoshi0418.attendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.takatoshi0418.attendance.domain.WorkTimeResult;
import com.github.takatoshi0418.attendance.domain.calculator.StandardAttendanceCalcurator;
import com.github.takatoshi0418.attendance.domain.entity.Attendance;
import com.github.takatoshi0418.attendance.domain.policy.AttendancePolicy;
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

        // List<DailyAttendanceView> dailyAttendanceViews = new ArrayList<>();
        LocalDate currentDate = startDateTime.toLocalDate();
        LocalDate nextMonthFirstDay = endDateTime.toLocalDate().plusDays(1);
        Map<LocalDate, Attendance> map = attendances.stream()
                .collect(Collectors.toMap(attendance -> attendance.getClockIn().toLocalDate(),
                        attendance -> attendance));
        List<DailyAttendanceView> dailyAttendanceViews = currentDate.datesUntil(nextMonthFirstDay).map(date -> {
            Attendance a = map.get(date);
            WorkTimeResult result = new StandardAttendanceCalcurator().calculate(date, a, policy);
            return DailyAttendanceView.from(result);
        }).toList();
        return MonthlyAttendanceView.from(targeYearMonth, dailyAttendanceViews);
    }
}
