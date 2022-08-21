package ru.matyuk.irregularVerbsBot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Compilation;
import ru.matyuk.irregularVerbsBot.model.CompilationVerb;
import ru.matyuk.irregularVerbsBot.model.CompilationVerbId;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.repository.CompilationVerbRepository;

import java.util.List;

@Component
public class CompilationVerbController {

    @Autowired
    private CompilationVerbRepository compilationVerbRepository;

    public void saveVerbsInGroup(Compilation compilation, List<Verb> verbs){
        for (Verb verb : verbs) {
            CompilationVerbId compilationVerbId = new CompilationVerbId(compilation.getId(), verb.getId());
            CompilationVerb compilationVerb = new CompilationVerb();
            compilationVerb.setCompilation(compilation);
            compilationVerb.setVerb(verb);
            compilationVerb.setId(compilationVerbId);
            compilationVerbRepository.save(compilationVerb);
        }
    }
}
