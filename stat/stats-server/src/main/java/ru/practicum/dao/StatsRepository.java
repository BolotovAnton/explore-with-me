package ru.practicum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


public interface StatsRepository extends JpaRepository<Stats, Integer> {

	@Query(value = "select new ru.practicum.dto.ViewStatsDto(s.app,s.uri,count(distinct s.ip)) " +
			"from Stats s " +
			"where s.timestamp between :start and :end " +
			"group by s.app,s.uri " +
			"order by count(distinct s.ip) desc "
	)
	List<ViewStatsDto> getStatsUnique(LocalDateTime start, LocalDateTime end);

	@Query(value = "select new ru.practicum.dto.ViewStatsDto(s.app,s.uri,count(s.ip)) " +
			"from Stats s " +
			"where s.timestamp between :start and :end " +
			"group by s.app,s.uri " +
			"order by count(s.ip) desc "
	)
	List<ViewStatsDto> getStatsNotUnique(LocalDateTime start, LocalDateTime end);

	@Query(value = "select new ru.practicum.dto.ViewStatsDto(s.app,s.uri,count(distinct s.ip)) " +
			"from Stats s " +
			"where s.timestamp between :start and :end " +
			"and s.uri in (:uris) " +
			"group by s.app,s.uri " +
			"order by count(distinct s.ip) desc "
	)
	List<ViewStatsDto> getStatsUniqueWithUris(LocalDateTime start, LocalDateTime end, Set<String> uris);

	@Query(value = "select new ru.practicum.dto.ViewStatsDto(s.app,s.uri,count(s.ip)) " +
			"from Stats s " +
			"where s.timestamp between :start and :end " +
			"and s.uri in (:uris) " +
			"group by s.app,s.uri " +
			"order by count(s.ip) desc "
	)
	List<ViewStatsDto> getStatsNotUniqueWithUris(LocalDateTime start, LocalDateTime end, Set<String> uris);
}
