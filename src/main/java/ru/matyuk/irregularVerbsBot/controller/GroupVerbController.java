package ru.matyuk.irregularVerbsBot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.model.id.GroupVerbId;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.repository.GroupVerbRepository;

import java.util.List;

@Component
public class GroupVerbController {

    @Autowired
    private GroupVerbRepository groupVerbRepository;

    public void saveVerbsInGroup(Group group, List<Verb> verbs){
        for (Verb verb : verbs) {
            GroupVerbId groupVerbId = new GroupVerbId(group.getId(), verb.getId());
            GroupVerb groupVerb = new GroupVerb();
            groupVerb.setGroup(group);
            groupVerb.setVerb(verb);
            groupVerb.setId(groupVerbId);
            groupVerbRepository.save(groupVerb);
        }
    }
    public void delete(List<GroupVerb> groupVerbList){
        for (GroupVerb groupVerb : groupVerbList) {
            groupVerbRepository.delete(groupVerb);
        }
    }

    public List<GroupVerb> getGroupVerbByIdGroup(Long groupId){
        return groupVerbRepository.findAllByGroupId(groupId);
    }
}
