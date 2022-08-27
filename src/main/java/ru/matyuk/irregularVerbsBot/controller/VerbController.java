package ru.matyuk.irregularVerbsBot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.repository.VerbRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class VerbController {

    @Autowired
    private VerbRepository verbRepository;

    public Verb getVerbByFirstForm(String firstForm){
        return verbRepository.findByFirstForm(firstForm);
    }

    public List<Verb> getVerbsByIds(List<Long> verbsId) {
        return verbRepository.findByIdIn(verbsId);
    }
}
