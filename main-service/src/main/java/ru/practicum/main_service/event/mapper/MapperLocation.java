package ru.practicum.main_service.event.mapper;

import ru.practicum.main_service.event.model.Location;
import ru.practicum.main_service.event.dto.LocationDto;

public class MapperLocation {

    public static Location toLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLon(locationDto.getLon());
        location.setLat(locationDto.getLat());
        return location;
    }

    public static LocationDto toLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());
        return locationDto;
    }
}
