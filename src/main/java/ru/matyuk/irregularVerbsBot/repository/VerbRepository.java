package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.matyuk.irregularVerbsBot.model.Verb;

@Repository
public interface VerbRepository extends CrudRepository<Verb, Long> {
}
