package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.model.GroupVerbId;

@Repository
public interface GroupVerbRepository extends CrudRepository<GroupVerb, GroupVerbId> {
}
