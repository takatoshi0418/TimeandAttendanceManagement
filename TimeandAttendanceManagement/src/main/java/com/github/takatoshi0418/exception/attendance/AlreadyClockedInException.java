package com.github.takatoshi0418.exception.attendance;

/**
 * すでに出勤打刻がある状態で出勤打刻を試みた場合に起こる例外クラス
 */
public class AlreadyClockedInException extends IllegalAttendanceException {

    /** メッセージ */
    private static final String MESSAGE = "出勤打刻を試みましたが、未退勤の勤怠記録が既に存在します。ユーザID: %d";

    /**
     * コンストラクタ
     * @param userId 対象のユーザID
     */
    public AlreadyClockedInException(long userId) {
        super(String.format(MESSAGE, userId));
    }

    /**
     * ユーザに表示するメッセージキーを取得する。
     * @return メッセージキー
     */
    @Override
    public String getMessageKey() {
        return "error.dashboard.alreadyClockedIn";
    }
}
