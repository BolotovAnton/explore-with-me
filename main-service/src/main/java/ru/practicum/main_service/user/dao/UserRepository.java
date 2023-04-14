package ru.practicum.main_service.user.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.user.model.User;

import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query(value = "select new ru.practicum.main_service.user.model.User(u.id, u.name, u.email) " +
			"from User as u " +
			"where u.id in (:ids) ")
	List<User> findUsers(Set<Long> ids, Pageable pageable);
}