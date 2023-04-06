package ru.practicum.service;

import ru.practicum.dto.StatsDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;
import java.util.Set;

public interface StatsService {

	StatsDto saveHit(StatsDto statsDto);

	List<ViewStatsDto> getStats(String start, String end, Set<String> uris, boolean unique);
}