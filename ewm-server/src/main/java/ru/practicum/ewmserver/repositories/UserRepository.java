package ru.practicum.ewmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmserver.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
