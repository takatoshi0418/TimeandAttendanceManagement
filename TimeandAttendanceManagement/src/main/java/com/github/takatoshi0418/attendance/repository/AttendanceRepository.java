package com.github.takatoshi0418.attendance.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.takatoshi0418.attendance.domain.Attendance;

import lombok.NonNull;


/**
 * AttendanceRepositoryインターフェース
 * 勤務記録を管理するためのリポジトリインターフェース
 */
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    /**
     * 指定したユーザの出勤中の勤務記録を取得する
     * @param userId 出勤中の勤務記録を取得するユーザのID
     * @return 出勤中の勤務記録のリスト
     */
    List<Attendance> findByUserIdAndClockOutIsNull(@NonNull Long userId);

    /**
     * 指定したユーザの最新の出勤中の勤務記録を取得する
     * @param userId 最新の出勤中の勤務記録をユーザのID
     * @return 最新の出勤中の勤務記録が存在する場合はOptionalにAttendanceを格納して返し、存在しない場合は空のOptionalを返す
     */
    Optional <Attendance> findTopByUserIdAndClockOutIsNullOrderByClockInDesc(@NonNull Long userId);

    /**
     * 指定したユーザおよび期間の勤怠記録を取得する
     * @param userId 勤怠記録をユーザID
     * @param start 指定した期間の開始日
     * @param end 指定した期間の終了日
     * @return 指定した期間の勤怠記録のリスト
     */
    List<Attendance> findByUserIdAndClockInBetweenOrderByClockInAsc(@NonNull Long userId, @NonNull LocalDateTime start, @NonNull LocalDateTime end);

}
