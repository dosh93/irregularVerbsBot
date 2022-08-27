package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.model.id.GroupVerbId;

import java.util.List;

@Repository
public interface GroupVerbRepository extends CrudRepository<GroupVerb, GroupVerbId> {

    List<GroupVerb> findAllByGroupId(Long groupId);
}
