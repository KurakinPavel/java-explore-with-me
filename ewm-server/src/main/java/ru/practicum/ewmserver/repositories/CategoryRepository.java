package ru.practicum.ewmserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmserver.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
