package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.matyuk.irregularVerbsBot.model.Learning;
import ru.matyuk.irregularVerbsBot.model.User;

import java.util.List;

public interface LearningRepository extends CrudRepository<Learning, Long> {

    List<Learning> findByUser(User user);
    List<Learning> findByUserAndCountSuccessfulLessThanOrderByCountSuccessfulAsc(User user, int countSuccessful);
    Learning findByUserAndState(User user, boolean state);
}
