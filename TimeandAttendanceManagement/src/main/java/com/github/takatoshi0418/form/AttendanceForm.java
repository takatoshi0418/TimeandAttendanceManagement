package com.github.takatoshi0418.form;

import java.time.LocalDateTime;

import com.github.takatoshi0418.entity.User;

public record AttendanceForm(User user, LocalDateTime timestamp) {
    
}
