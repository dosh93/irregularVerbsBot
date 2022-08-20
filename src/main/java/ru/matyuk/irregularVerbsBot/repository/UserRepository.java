package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.matyuk.irregularVerbsBot.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
