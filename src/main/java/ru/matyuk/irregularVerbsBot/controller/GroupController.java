package ru.matyuk.irregularVerbsBot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.repository.GroupRepository;

import java.util.*;

@Slf4j
@Component
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;


    public List<Group> getGroupsWithVerbsByChatId(Long chatId){
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(chatId);
        List<Group> all = groupRepository.findByUserChatIdIn(ids);
        List<Group> result = new ArrayList<>();
        for (Group groupVerb : all) {
            if(!groupVerb.getVerbs().isEmpty()) result.add(groupVerb);
        }
        return result;
    }

    public Group getGroup(String name){
        return groupRepository.findByName(name);
    }

    public Group createGroup(User user){
        Group group = new Group();
        group.setUser(user);
        group.setName(user.getChatId().toString());
        return groupRepository.save(group);
    }

    public Group getGroup(Long groupId) {
        return groupRepository.findById(groupId).get();
    }

    public Group setName(Group group, String messageText) {
        group.setName(messageText);
        return groupRepository.save(group);
    }

    public void delete(Long idGroup) {
        groupRepository.deleteById(idGroup);
    }

    public void delete(List<Group> groups) {
        groups.forEach(group -> groupRepository.delete(group));
    }
}
