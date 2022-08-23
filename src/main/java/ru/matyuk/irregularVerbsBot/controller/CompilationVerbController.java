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

    @Autowired
    private VerbController verbController;

    @Autowired GroupVerbController groupVerbController;

    public void saveVerbsInGroup(Long compilationId, List<Long> verbId){
        List<Verb> verbs = verbController.getVerbsByIds(verbId);
        Compilation compilation = groupVerbController.getGroup(compilationId);
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
