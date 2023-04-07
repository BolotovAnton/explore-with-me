package ru.practicum.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dao.StatsRepository;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mapper.Mapper;
import ru.practicum.model.Stats;
import ru.practicum.util.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
@Log
public class StatsServiceImpl implements StatsService {

	private final StatsRepository statsRepository;

	public StatsServiceImpl(StatsRepository statsRepository) {
		this.statsRepository = statsRepository;
	}

	@Override
	@Transactional
	public StatsDto saveHit(StatsDto statsDto) {
		Stats savedStats = statsRepository.save(Mapper.toStat(statsDto));
		log.info("stats saved");
		return Mapper.toStatDto(savedStats);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ViewStatsDto> getStats(String start, String end, Set<String> uris, boolean unique) {
		LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern(Constants.DATEFORMAT));
		LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern(Constants.DATEFORMAT));
		log.info("get stats");
		if (uris == null) {
			if (unique) {
				return statsRepository.getStatsUnique(startDate, endDate);
			} else {
				return statsRepository.getStatsNotUnique(startDate, endDate);
			}
		} else {
			if (unique) {
				return statsRepository.getStatsUniqueWithUris(startDate, endDate, uris);
			} else {
				return statsRepository.getStatsNotUniqueWithUris(startDate, endDate, uris);
			}
		}
	}
}
