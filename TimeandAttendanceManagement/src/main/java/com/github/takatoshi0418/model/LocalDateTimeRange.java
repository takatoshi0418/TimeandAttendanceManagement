package com.github.takatoshi0418.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 日付の期間を扱うクラス。
 */
public class LocalDateTimeRange {

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    /**
     * 指定した開始日時と終了日時による期間の生成。
     * @param startDateTime 開始日時
     * @param endDateTime   終了日時
     */
    public LocalDateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * 指定した開始時間と終了時間による期間の生成。
     * 終了時間が開始時間より前の場合は、翌日の終了とみなして期間を計算。
     * @param startTime 開始時間
     * @param endTime   終了時間
     */
    public LocalDateTimeRange(LocalTime startTime, LocalTime endTime) {
        this.startDateTime = LocalDate.EPOCH.atTime(startTime);
        if (startTime.isBefore(endTime)) {
            this.endDateTime = LocalDate.EPOCH.atTime(endTime);
        } else {
            this.endDateTime = LocalDate.EPOCH.plusDays(1).atTime(endTime);
        }
    }

    /**
     * 指定した開始日と終了日による期間の生成。
     * 開始日の00:00:00から終了日の23:59:59.999...までを対象。
     * @param startDate 開始日
     * @param endDate   終了日
     */
    public LocalDateTimeRange(LocalDate startDate, LocalDate endDate) {
        this.startDateTime = startDate.atTime(LocalTime.MIN);
        this.endDateTime = endDate.atTime(LocalTime.MAX);
    }

    /**
     * 開始日時の取得。
     * @return 開始日時
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * 終了日時の取得。
     * @return 終了日時
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * 開始日の取得。
     * @return 開始日
     */
    public LocalDate getStartDate() {
        return startDateTime.toLocalDate();
    }

    /**
     * 終了日の取得。
     * @return 終了日
     */
    public LocalDate getEndDate() {
        return endDateTime.toLocalDate();
    }

    /**
     * 開始時間の取得。
     * @return 開始時間
     */
    public LocalTime getStartTime() {
        return startDateTime.toLocalTime();
    }

    /**
     * 終了時間の取得。
     * @return 終了時間
     */
    public LocalTime getEndTime() {
        return endDateTime.toLocalTime();
    }

    /**
     * 開始日時と終了日時の期間の取得。
     * @return 期間
     */
    public Duration getDuration() {
        return Duration.between(startDateTime, endDateTime);
    }

    /**
     * 期間の日数の取得。
     * @return 期間（日数）
     */
    public long getBetweenDays() {
        return getDuration().toDays();
    }

    /**
     * 期間の時間の取得。
     * @return 期間（時間）
     */
    public long getBetweenHours() {
        return getDuration().toHours();
    }

    /**
     * 期間の分数の取得。
     * @return 期間（分）
     */
    public long getBetweenMinutes() {
        return getDuration().toMinutes();
    }

    /**
     * 指定した日時が範囲内であるかの判定。
     * @param datetime 判定対象の日時
     * @return 期間内の場合はtrue
     */
    public boolean isBetween(LocalDateTime datetime) {
        return !datetime.isBefore(startDateTime) && !datetime.isAfter(endDateTime);
    }

    /**
     * 指定した日付が範囲内であるかの判定。
     * @param date 判定対象の日付
     * @return 期間内の場合はtrue
     */
    public boolean isBetween(LocalDate date) {
        return !date.isBefore(getStartDate()) && !date.isAfter(getEndDate());
    }

    /**
     * 指定した日付が範囲内であるかの判定。
     * @param time 判定対象の日付
     * @return 期間内の場合はtrue
     */
    public boolean isBetween(LocalTime time) {
        return !time.isBefore(getStartTime()) && !time.isAfter(getEndTime());
    }    
}