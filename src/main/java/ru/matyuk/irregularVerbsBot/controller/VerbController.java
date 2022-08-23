package ru.matyuk.irregularVerbsBot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Compilation;
import ru.matyuk.irregularVerbsBot.model.CompilationVerb;
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

    public List<Verb> getVerbsByGroup(Compilation compilation){
        return compilation.getVerbs().stream().map(CompilationVerb::getVerb).collect(Collectors.toList());
    }

    public List<Verb> getVerbsByIds(List<Long> verbsId) {
        return verbRepository.findByIdIn(verbsId);
    }
}
