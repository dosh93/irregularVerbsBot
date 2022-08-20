package ru.matyuk.irregularVerbsBot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.repository.GroupVerbRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GroupVerbController {

    @Autowired
    private GroupVerbRepository groupVerbRepository;


    public List<GroupVerb> getGroupsWithVerbs(){
        List<GroupVerb> all = (List<GroupVerb>) groupVerbRepository.findAll();
        List<GroupVerb> result = new ArrayList<>();
        for (GroupVerb groupVerb : all) {
            if(!groupVerb.getVerbs().isEmpty()) result.add(groupVerb);
        }
        return result;

    }

    public GroupVerb getGroup(String name){
        return groupVerbRepository.findByName(name);
    }

}
