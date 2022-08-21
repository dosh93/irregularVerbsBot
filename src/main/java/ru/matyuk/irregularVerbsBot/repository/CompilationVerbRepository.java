package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.matyuk.irregularVerbsBot.model.CompilationVerb;
import ru.matyuk.irregularVerbsBot.model.CompilationVerbId;

@Repository
public interface CompilationVerbRepository extends CrudRepository<CompilationVerb, CompilationVerbId> {
}
