package com.github.takatoshi0418.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.takatoshi0418.entity.Attendance;

import lombok.NonNull;


/**
 * AttendanceRepositoryインターフェース
 * 勤務記録を管理するためのリポジトリインターフェース
 */
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    /**
     * 指定したユーザの出勤中の勤務記録を取得する
     * @param userId 出勤中の勤務記録を取得するユーザのID
     * @return 出勤中の勤務記録が存在する場合はOptionalにAttendanceを格納して返し、存在しない場合は空のOptionalを返す
     */
    Optional<Attendance> findByUserIdAndClockOutIsNull(@NonNull Long userId);
}
