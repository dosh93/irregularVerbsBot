package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.matyuk.irregularVerbsBot.model.Compilation;

import java.util.List;

@Repository
public interface GroupVerbRepository extends CrudRepository<Compilation, Long> {

    Compilation findByName(String name);
    List<Compilation> findByUserChatIdIn(List<Long> ids);

}
