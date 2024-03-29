package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.matyuk.irregularVerbsBot.model.Verb;

import java.util.List;

@Repository
public interface VerbRepository extends CrudRepository<Verb, Long> {

    Verb findByFirstForm(String firstForm);

    List<Verb> findByIdIn(List<Long> verbsId);
}
