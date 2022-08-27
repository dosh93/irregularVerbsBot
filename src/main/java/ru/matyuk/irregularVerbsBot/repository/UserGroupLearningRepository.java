package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.matyuk.irregularVerbsBot.model.UserGroupLearning;
import ru.matyuk.irregularVerbsBot.model.id.UserGroupId;

public interface UserGroupLearningRepository extends CrudRepository<UserGroupLearning, UserGroupId> {
}
