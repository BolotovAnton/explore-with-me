package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsDto {
	private Integer id;
	private String app;
	private String uri;
	private String ip;
	private String timestamp;
}
