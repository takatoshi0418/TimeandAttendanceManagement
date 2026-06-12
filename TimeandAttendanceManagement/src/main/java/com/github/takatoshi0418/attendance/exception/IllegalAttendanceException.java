package com.github.takatoshi0418.attendance.exception;

/**
 * 不正な勤怠操作が行われたときに起こる例外クラス
 */
public abstract class IllegalAttendanceException extends Exception {

    /**
     * コンストラクタ
     * @param message 例外の詳細なメッセージ
     */
    public IllegalAttendanceException(String message) {
        super(message);
    }

    /**
     * コンストラクタ
     * @param message 例外の詳細なメッセージ
     * @param cause 原因となった例外
     */
    public IllegalAttendanceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * ユーザに表示するメッセージキーを取得する。
     * @return メッセージキー
     */
    public abstract String getMessageKey();
}
