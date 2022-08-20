package ru.matyuk.irregularVerbsBot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Compilation;
import ru.matyuk.irregularVerbsBot.repository.GroupVerbRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GroupVerbController {

    @Autowired
    private GroupVerbRepository groupVerbRepository;


    public List<Compilation> getGroupsWithVerbs(){
        List<Compilation> all = (List<Compilation>) groupVerbRepository.findAll();
        List<Compilation> result = new ArrayList<>();
        for (Compilation groupVerb : all) {
            if(!groupVerb.getVerbs().isEmpty()) result.add(groupVerb);
        }
        return result;

    }

    public Compilation getGroup(String name){
        return groupVerbRepository.findByName(name);
    }

}
