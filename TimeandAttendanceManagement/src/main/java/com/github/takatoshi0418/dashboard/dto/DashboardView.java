package com.github.takatoshi0418.dashboard.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.NonNull;

/** ダッシュボード画面の表示情報 */
public record DashboardView(
    @NonNull TodayAttendanceView todayAttendance
) {
    /**
     * サーバーの現在時刻をフォーマット
     * @return フォーマットされた現在時刻
     */
    public String formatedServerDatetime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
