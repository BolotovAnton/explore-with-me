package ru.practicum.stats_client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats_dto.Constants;
import ru.practicum.stats_dto.model.StatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatsClient extends BaseClient {
    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> saveHit(String appName, String uri, String ip, LocalDateTime timestamp) {
        StatsDto statsDto = new StatsDto();
        statsDto.setApp(appName);
        statsDto.setUri(uri);
        statsDto.setIp(ip);
        statsDto.setTimestamp(timestamp.format(Constants.DATE_TIME_FORMATTER));
        log.info("hit has been saved");
        return post("hit/", statsDto);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        return getStats(start, end, uris, null);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end) {
        return getStats(start, end, null, null);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, Boolean unique) {
        return getStats(start, end, null, unique);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("invalid time period");
        }
        StringBuilder uriBuilder = new StringBuilder("stats/?start={start}&end={end}");
        Map<String, Object> parameters = Map.of(
                "start", start.format(Constants.DATE_TIME_FORMATTER),
                "end", end.format(Constants.DATE_TIME_FORMATTER)
        );
        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                uriBuilder.append("&uris=").append(uri);
            }
        }
        if (unique != null) {
            uriBuilder.append("&unique=").append(unique);
        }
        log.info("stats has been received");
        return get(uriBuilder.toString(), parameters);
    }
}
