package ru.matyuk.irregularVerbsBot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.UserGroupLearning;
import ru.matyuk.irregularVerbsBot.repository.GroupRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupVerbController groupVerbController;

    @Autowired UserGroupLearningController userGroupLearningController;


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

    public  List<Group> getGroupNotLearning(User user){
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(user.getChatId());
        List<Long> groupIds = new ArrayList<>();
        List<Group> all;
        List<UserGroupLearning> groupLearnings = user.getGroupLearnings();
        if(groupLearnings.size() > 0){
            groupIds.addAll(groupLearnings.stream().map(UserGroupLearning::getGroup).map(Group::getId).collect(Collectors.toList()));
            all = groupRepository.findByUserChatIdInAndIdNotIn(ids, groupIds);
        }else {
            all = groupRepository.findByUserChatIdIn(ids);
        }
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
        Group group = getGroup(idGroup);
        groupVerbController.delete(group.getVerbs());
        userGroupLearningController.delete(group.getUserLearns());
        groupRepository.deleteById(idGroup);
    }

    public void delete(List<Group> groups) {
        groups.forEach(group -> groupRepository.delete(group));
    }

    public void delete(String name) {
        groupRepository.delete(getGroup(name));
    }
}
