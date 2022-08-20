package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.matyuk.irregularVerbsBot.model.Compilation;

@Repository
public interface GroupVerbRepository extends CrudRepository<Compilation, Long> {

    Compilation findByName(String name);

}
