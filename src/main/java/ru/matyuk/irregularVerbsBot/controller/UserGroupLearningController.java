package ru.matyuk.irregularVerbsBot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.UserGroupLearning;
import ru.matyuk.irregularVerbsBot.repository.UserGroupLearningRepository;

import java.util.List;

@Component
public class UserGroupLearningController {

    @Autowired
    private UserGroupLearningRepository userGroupLearningRepository;


    public void delete(UserGroupLearning userGroupLearning){
        userGroupLearningRepository.delete(userGroupLearning);
    }

    public void delete(List<UserGroupLearning> userGroupLearningList){
        userGroupLearningList.forEach(this::delete);
    }

    public UserGroupLearning getByUserAndGroup(User user, Group group) {
        return userGroupLearningRepository.findByUserChatIdAndGroupId(user.getChatId(), group.getId());
    }
}
