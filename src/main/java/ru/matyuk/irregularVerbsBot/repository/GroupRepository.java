package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.matyuk.irregularVerbsBot.model.Group;

import java.util.List;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {

    Group findByName(String name);
    List<Group> findByUserChatIdIn(List<Long> ids);
}
