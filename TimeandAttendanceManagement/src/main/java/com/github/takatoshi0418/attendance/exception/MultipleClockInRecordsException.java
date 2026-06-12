package com.github.takatoshi0418.attendance.exception;

/**
 * 複数の出勤打刻が存在するときに起こる例外クラス
 */
public class MultipleClockInRecordsException extends IllegalAttendanceException {
    
    /** メッセージ */
    private static final String MESSAGE = "複数の出勤打刻が存在します。ユーザID: %d";

    /**
     * コンストラクタ
     * @param userId ユーザID
     */
	public MultipleClockInRecordsException(long userId) {
		super(String.format(MESSAGE, userId));
	}

    /**
     * ユーザに表示するメッセージキーを取得する。
     * @return メッセージキー
     */
    @Override
    public String getMessageKey() {
        return "error.dashboard.multipleAttendance";
    }
}
