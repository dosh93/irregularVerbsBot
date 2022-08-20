package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.model.Verb;

import java.util.List;

@Repository
public interface GroupVerbRepository extends CrudRepository<GroupVerb, Long> {

    GroupVerb findByName(String name);

}
