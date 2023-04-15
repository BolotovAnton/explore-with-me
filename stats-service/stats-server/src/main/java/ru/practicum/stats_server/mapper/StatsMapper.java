package ru.practicum.stats_server.mapper;

import ru.practicum.stats_dto.Constants;
import ru.practicum.stats_dto.model.StatsDto;
import ru.practicum.stats_server.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {

    public static Stats toStats(StatsDto statsDto) {
        Stats stats = new Stats();
        stats.setApp(statsDto.getApp());
        stats.setIp(statsDto.getIp());
        stats.setUri(statsDto.getUri());
        stats.setTimestamp(LocalDateTime.parse(
                statsDto.getTimestamp(),
                DateTimeFormatter.ofPattern(Constants.DATEFORMAT))
        );
        return stats;
    }
}
