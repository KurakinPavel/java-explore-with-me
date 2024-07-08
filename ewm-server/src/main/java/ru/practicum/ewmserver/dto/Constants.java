package ru.practicum.ewmserver.dto;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final int FREE_TIME_INTERVAL = 100;
    public static final int UPDATE_TIME_LIMIT_ADMIN = 1;
    public static final int UPDATE_TIME_LIMIT_USER = 2;
    public static final int CHANGING_RATING_WHEN_CHANGING_MARK = 2;
    public static final int RATING_CHANGE_AT_NEW_MARK_OR_DELETE_MARK = 1;
}
