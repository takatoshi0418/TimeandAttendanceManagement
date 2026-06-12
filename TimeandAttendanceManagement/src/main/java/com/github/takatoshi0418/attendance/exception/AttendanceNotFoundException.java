package com.github.takatoshi0418.attendance.exception;

/**
 * 退勤が存在しない状態で退勤を試みたときに起こる例外クラス
 */
public class AttendanceNotFoundException extends IllegalAttendanceException {
    
	/** メッセージ */
    private static final String MESSAGE = "退勤打刻を試みましたが、指定された出勤記録が存在しません。ユーザID: %d";

	/**
	 * コンストラクタ
	 * @param userId ユーザID
	 */
	public AttendanceNotFoundException(long userId) {
		super(String.format(MESSAGE, userId));
	}

	/**
	 * ユーザに表示するメッセージキーを取得する。
	 * @return メッセージキー
	 */
	@Override
    public String getMessageKey() {
        return "error.dashboard.attendanceNotFound";
    }
}
