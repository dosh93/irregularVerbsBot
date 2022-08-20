package ru.matyuk.irregularVerbsBot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.repository.VerbRepository;

import java.util.List;

@Component
@Slf4j
public class VerbController {

    @Autowired
    private VerbRepository verbRepository;

    public List<Verb> getVerbs(){
        return (List<Verb>) verbRepository.findAll();
    }
    public Verb getVerb(long id){
        return verbRepository.findById(id).get();
    }

    public List<Verb> getVerbsByGroupId(GroupVerb groupVerb){
        return verbRepository.findVerbsByGroups(groupVerb);
    }

}
