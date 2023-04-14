package ru.practicum.stats_server.service;

import ru.practicum.stats_dto.model.StatsDto;
import ru.practicum.stats_dto.model.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void saveHit(StatsDto statsDto);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
