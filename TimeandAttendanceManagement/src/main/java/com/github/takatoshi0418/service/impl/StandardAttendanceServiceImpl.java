package com.github.takatoshi0418.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.takatoshi0418.entity.Attendance;
import com.github.takatoshi0418.entity.User;
import com.github.takatoshi0418.repository.AttendanceRepository;
import com.github.takatoshi0418.service.AttendanceService;

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
     * ユーザの出勤処理を行うメソッド
     * @param user 出勤するユーザ
     */
    @Override
    @Transactional
    public void clockIn(@NonNull User user) {
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setClockIn(java.time.LocalDateTime.now());
        attendanceRepository.save(attendance);

        logger.info("{}さんが出勤しました。", user.getFullName());
    }

    /**
     * ユーザの退勤処理を行うメソッド
     * @param user 退勤するユーザ
     */
    @Override
    @Transactional
    public void clockOut(@NonNull User user) {
        Attendance attendance = attendanceRepository.findByUserIdAndClockOutIsNull(user.getId())
                .orElseThrow(() -> new IllegalStateException("出勤記録が見つかりません"));
        attendance.setUser(user);
        attendance.setClockOut(java.time.LocalDateTime.now());
        attendanceRepository.save(attendance);

        logger.info("{}さんが退勤しました。", user.getFullName());
    }

    /**
     * ユーザが出勤中かどうかを確認するメソッド
     * @param user 確認するユーザ
     * @return 出勤中の場合はtrue、それ以外はfalse
     */
    @Override
    public boolean isClockedIn(@NonNull User user) {
        return attendanceRepository.findByUserIdAndClockOutIsNull(user.getId()).isPresent();
    }
}
