package ru.practicum.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats_dto.model.ViewStatsDto;
import ru.practicum.stats_server.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {
    @Query("SELECT new ru.practicum.stats_dto.model.ViewStatsDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStatsDto> getAllStatsDistinctIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats_dto.model.ViewStatsDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStatsDto> getAllStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats_dto.model.ViewStatsDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "AND s.uri IN (?3) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStatsDto> getStatsByUrisDistinctIp(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("SELECT new ru.practicum.stats_dto.model.ViewStatsDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "AND s.uri IN (?3) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStatsDto> getStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uri);
}