package com.github.takatoshi0418.attendance.exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ClockingDuplicateException extends IllegalAttendanceException {

    /** メッセージ */
    private static final String MESSAGE = "同じ日にすでに打刻データが存在します。ユーザID: %d, 対象日:%s";

	/**
	 * コンストラクタ
	 * @param userId ユーザID
	 */
	public ClockingDuplicateException(long userId, LocalDate targetDate) {
		super(String.format(MESSAGE, userId,targetDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
	}

    @Override
    public String getMessageKey() {
        return "error.dashboard.clockingDuplicate";
    }
    
}
