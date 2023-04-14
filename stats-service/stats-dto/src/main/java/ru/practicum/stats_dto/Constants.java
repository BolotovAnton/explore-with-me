package ru.practicum.stats_dto;

import java.time.format.DateTimeFormatter;

public abstract class Constants {

    public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATEFORMAT);
}
