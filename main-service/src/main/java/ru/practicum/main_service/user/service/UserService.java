package ru.practicum.main_service.user.service;

import ru.practicum.main_service.user.dto.UserCreateDto;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {

	UserDto addUser(UserCreateDto userCreateDto);

	List<UserDto> findAllUsers(Set<Long> ids, Integer from, Integer size);

	void deleteUser(Long userId);

    User findUserById(Long id);
}