package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.matyuk.irregularVerbsBot.model.Learning;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.id.UserVerbId;

import java.util.List;

@Repository
public interface LearningRepository extends CrudRepository<Learning, UserVerbId> {

    List<Learning> findByUserAndCountSuccessfulLessThanOrderByCountSuccessfulAsc(User user, int countSuccessful);
    Learning findByUserAndState(User user, boolean state);
}
