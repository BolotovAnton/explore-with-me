package ru.practicum.mapper;

import ru.practicum.dto.StatsDto;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {

    public static Stats toStat(StatsDto statsDto) {
        Stats stats = new Stats();
        stats.setApp(statsDto.getApp());
        stats.setIp(statsDto.getIp());
        stats.setUri(statsDto.getUri());
        stats.setTimestamp(LocalDateTime.parse(
                statsDto.getTimestamp(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        return stats;
    }

    public static StatsDto toStatDto(Stats stats) {
        StatsDto statsDto = new StatsDto();
        statsDto.setApp(stats.getApp());
        statsDto.setIp(stats.getIp());
        statsDto.setUri(stats.getUri());
        statsDto.setTimestamp(stats.getTimestamp().toString());
        return statsDto;
    }
}