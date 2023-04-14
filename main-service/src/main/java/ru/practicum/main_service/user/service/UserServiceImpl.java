package ru.practicum.main_service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.user.dao.UserRepository;
import ru.practicum.main_service.user.dto.UserCreateDto;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.mapper.MapperUser;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addUser(UserCreateDto userCreateDto) {
        UserDto savedUserDto = MapperUser.toUserDto(userRepository.save(MapperUser.toUser(userCreateDto)));
        log.info("user has been added " + savedUserDto);
        return savedUserDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers(Set<Long> ids, Integer from, Integer size) {
        List<UserDto> userDtoList = new ArrayList<>();
        userRepository.findUsers(ids, PageRequest.of(from / size, size))
                .forEach(x -> userDtoList.add(MapperUser.toUserDto(x)));
        log.info("list of users have been gotten " + userDtoList);
        return userDtoList;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.delete(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("delete error")));
        log.info("user with id = " + userId + " has been deleted");
    }

    @Override
    public User findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user is not found"));
        log.info("user with id = " + userId + " has been found");
        return user;
    }
}