package ru.practicum.stats_dto.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ViewStatsDto {

    private String app;

    private String uri;

    private Long hits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewStatsDto viewStatsDto = (ViewStatsDto) o;
        return Objects.equals(app, viewStatsDto.app) && Objects.equals(uri, viewStatsDto.uri) && Objects.equals(hits, viewStatsDto.hits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri, hits);
    }
}
