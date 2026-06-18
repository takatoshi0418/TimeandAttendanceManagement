package com.github.takatoshi0418.attendance.exception;

public class ClockingDuplicateException extends IllegalAttendanceException {

    /** メッセージ */
    private static final String MESSAGE = "同じ日にすでに打刻データが存在します。ユーザID: %d";

	/**
	 * コンストラクタ
	 * @param userId ユーザID
	 */
	public ClockingDuplicateException(long userId) {
		super(String.format(MESSAGE, userId));
	}

    @Override
    public String getMessageKey() {
        return "error.dashboard.clockingDuplicate";
    }
    
}
