package ru.practicum.main_service.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.RequestRepository;
import ru.practicum.stats_client.StatsClient;
import ru.practicum.stats_dto.Constants;
import ru.practicum.stats_dto.model.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsClient statsClient;
    private final RequestRepository requestRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void addHit(HttpServletRequest request) {
        statsClient.saveHit(
                "main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.parse(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER), Constants.DATE_TIME_FORMATTER)
        );
        log.info("request to register a call to the statistics server has been sent");
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        ResponseEntity<Object> response = statsClient.getStats(start, end, uris, unique);
        log.info("a request to get statistics to the server has been sent");
        try {
            return Arrays.asList(mapper.readValue(mapper.writeValueAsString(response.getBody()), ViewStatsDto[].class));
        } catch (IOException exception) {
            throw new ClassCastException(exception.getMessage());
        }
    }

    @Override
    public Map<Long, Long> getViews(List<Event> events) {
        Map<Long, Long> views = new HashMap<>();
        List<Event> publishedEvents = events.stream()
                .filter(event -> event.getPublishedOn() != null)
                .collect(Collectors.toList());;
        if (events.isEmpty()) {
            return views;
        }
        Optional<LocalDateTime> minPublishedOn = publishedEvents.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);

        if (minPublishedOn.isPresent()) {
            LocalDateTime start = minPublishedOn.get();
            LocalDateTime end = LocalDateTime.now();
            List<String> uris = publishedEvents.stream()
                    .map(Event::getId)
                    .map(id -> ("/events/" + id))
                    .collect(Collectors.toList());

            List<ViewStatsDto> stats = getStats(start, end, uris, null);
            stats.forEach(stat -> {
                Long eventId = Long.parseLong(stat.getUri()
                        .split("/", 0)[2]);
                views.put(eventId, views.getOrDefault(eventId, 0L) + stat.getHits());
            });
        }
        log.info("a request to get statistics of non-unique visits has been sent");
        return views;
    }

    @Override
    public Map<Long, Long> getViews(Event event) {
        Map<Long, Long> views = new HashMap<>();
        if (event.getPublishedOn() == null) {
            return views;
        }
        LocalDateTime start = event.getPublishedOn();
        LocalDateTime end = LocalDateTime.now();
        String uri = "/events/" + event.getId();

        List<ViewStatsDto> stats = getStats(start, end, Collections.singletonList(uri), null);
        stats.forEach(stat -> {
            Long eventId = Long.parseLong(stat.getUri()
                    .split("/", 0)[2]);
            views.put(eventId, views.getOrDefault(eventId, 0L) + stat.getHits());
        });
        return views;
    }

    @Override
    public Map<Long, Long> getConfirmedRequests(List<Event> events) {
        List<Long> eventsId = events.stream()
                .filter(event -> event.getPublishedOn() != null)
                .map(Event::getId)
                .collect(Collectors.toList());
        Map<Long, Long> requestStats = new HashMap<>();
        if (!eventsId.isEmpty()) {
            requestRepository.findConfirmedRequests(eventsId)
                    .forEach(stat -> requestStats.put(stat.getEventId(), stat.getConfirmedRequests()));
        }
        log.info("a request to get confirmed requests has been sent");
        return requestStats;
    }

    @Override
    public Map<Long, Long> getConfirmedRequests(Event event) {
        Map<Long, Long> requestStats = new HashMap<>();
        if (event.getPublishedOn() == null) {
            return requestStats;
        }
        requestRepository.findConfirmedRequests(Collections.singletonList(event.getId()))
                .forEach(stat -> requestStats.put(stat.getEventId(), stat.getConfirmedRequests()));
        return requestStats;
    }
}
