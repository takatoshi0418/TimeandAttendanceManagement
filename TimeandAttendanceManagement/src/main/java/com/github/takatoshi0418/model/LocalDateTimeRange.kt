package com.github.takatoshi0418.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 日付の期間を扱うクラス
 */
class LocalDateTimeRange(val startDateTime: LocalDateTime, val endDateTime: LocalDateTime) {

    /**
     * コンストラクタ
     * 終了日が開始日より前の場合、日付を跨ぐ
     * @param startTime 開始時間
     * @param endTime 終了時間
    */
    constructor(startTime: LocalTime, endTime: LocalTime): this(
        LocalDate.EPOCH.atTime(startTime),
        if (startTime.isBefore(endTime)) {
            LocalDate.EPOCH.atTime(endTime)
        } else {
            LocalDate.EPOCH.plusDays(1).atTime(endTime)
        }
    )

    /**
     * コンストラクタ
     * @param startDate 開始日
     * @param endDate 終了日
    */
    constructor(startDate: LocalDate, endDate: LocalDate): this(
        startDate.atTime(LocalTime.MIN),
        endDate.atTime(LocalTime.MAX)
    )

    /**
     * 開始日を取得する
     * @return 開始日
     */
    public fun getStartDate(): LocalDate {
        return startDateTime.toLocalDate();
    }

    /**
     * 終了日を取得する
     * @return 終了日
     */
    public fun getEndDate(): LocalDate {
        return endDateTime.toLocalDate();
    }

    /**
     * 開始時間を取得する
     * @return 開始時間
     */
    public fun getStartTime(): LocalTime {
        return startDateTime.toLocalTime();
    }

    /**
     * 終了時間を取得する
     * @return 終了時間
     */
    public fun getEndTime(): LocalTime {
        return endDateTime.toLocalTime();
    }

    /**
     * 開始日時と終了日時の期間を取得する
     * @return 期間
     */
    public fun getDuration(): Duration {
        return Duration.between(startDateTime, endDateTime);
    }

    public fun getBetweenDays(): Long {
        return getDuration().toDays();
    }

    public fun getBetweenHours(): Long {
        return getDuration().toHours();
    }

    public fun getBetweenMinutes(): Long {
        return getDuration().toMinutes();
    }
}
