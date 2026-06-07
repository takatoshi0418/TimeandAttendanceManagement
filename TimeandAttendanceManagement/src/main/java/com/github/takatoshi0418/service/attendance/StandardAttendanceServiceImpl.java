package com.github.takatoshi0418.service.attendance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.takatoshi0418.entity.Attendance;
import com.github.takatoshi0418.entity.User;
import com.github.takatoshi0418.exception.attendance.AlreadyClockedInException;
import com.github.takatoshi0418.exception.attendance.AttendanceNotFoundException;
import com.github.takatoshi0418.exception.attendance.IllegalAttendanceException;
import com.github.takatoshi0418.exception.attendance.MultipleClockInRecordsException;
import com.github.takatoshi0418.repository.AttendanceRepository;

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
     * @param user 出勤するユーザ
     * @throws IllegalAttendanceException 既に出勤記録が存在する場合
     */
    @Override
    @Transactional
    public void clockIn(@NonNull User user) throws IllegalAttendanceException  {
        
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
     * @param user 確認するユーザ
     * @return 出勤中の場合はtrue、それ以外はfalse
     */
    @Override
    public boolean isClockedIn(@NonNull User user) {
        return attendanceRepository.findByUserIdAndClockOutIsNull(user.getId()).size() > 0;
    }

    /**
     * 最新の出勤情報を取得する
     * @param user 取得したいユーザ
     * @return 最新の出勤情報、存在しない場合は空のOptional
     */
    public Optional<Attendance> getLatestAttendance(@NonNull User user) {
        return attendanceRepository.findTopByUserIdAndClockOutIsNullOrderByClockInDesc(user.getId());
    }
}
