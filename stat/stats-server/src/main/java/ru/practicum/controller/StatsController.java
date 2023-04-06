package ru.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsServiceImpl;

import java.util.List;
import java.util.Set;

@RestController
public class StatsController {

	private final StatsServiceImpl statsServiceImpl;

	public StatsController(StatsServiceImpl statsServiceImpl) {
		this.statsServiceImpl = statsServiceImpl;
	}

	@PostMapping("/hit")
	@ResponseStatus(HttpStatus.CREATED)
	public StatsDto createStat(@RequestBody StatsDto statsDto) {
		return statsServiceImpl.saveHit(statsDto);
	}

	@GetMapping("/stats")
	@ResponseStatus(HttpStatus.OK)
	public List<ViewStatsDto> getStats(@RequestParam(name = "start") String start,
									   @RequestParam(name = "end") String end,
									   @RequestParam(name = "uris", required = false) Set<String> uris,
									   @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
		return statsServiceImpl.getStats(start, end, uris, unique);
	}
}
