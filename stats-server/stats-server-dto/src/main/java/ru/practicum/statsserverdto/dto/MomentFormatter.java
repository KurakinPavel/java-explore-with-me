package ru.practicum.statsserverdto.dto;

import java.time.format.DateTimeFormatter;

public class MomentFormatter {
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
