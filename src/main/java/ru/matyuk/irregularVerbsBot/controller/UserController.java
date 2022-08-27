package ru.matyuk.irregularVerbsBot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.model.Learning;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.repository.UserRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupController groupController;

    @Autowired
    private LearningController learningController;

    @Autowired
    private GroupVerbController groupVerbController;

    @Autowired
    private FeedbackController feedbackController;

    @Value("${learning.count_successful}")
    Integer countSuccessful;


    public User registerUser(Chat chat) {
        User user = new User();
        var chatId = chat.getId();
        Optional<User> optionalUser = userRepository.findById(chatId);
        if(optionalUser.isEmpty()) {

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setCountSuccessful(countSuccessful);
            user.setRegisterAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("Сохранен юзер " + user);
        }else user = optionalUser.get();

        return user;
    }

    public User getUser(long chatId){
        if (userRepository.findById(chatId).isEmpty())
            return null;
        return userRepository.findById(chatId).get();
    }

    public User setState(User user, StateUser state){
        user.setState(state);
        userRepository.save(user);
        return user;
    }

    public boolean isLearning(User user){
        return user.getLearnings().size() != 0;
    }

    public User setTmp(User user, String tmp){
        user.setTmp(tmp);
        return userRepository.save(user);
    }

    public void delete(User user) {
        List<Learning> learnings = user.getLearnings();
        List<GroupVerb> groupVerbList = new ArrayList<>();

        learningController.delete(learnings);

        user = userRepository.findById(user.getChatId()).get();
        List<Group> groups = user.getGroups();

        groups.forEach(group -> groupVerbList.addAll(group.getVerbs()));
        groupVerbController.delete(groupVerbList);

        user = userRepository.findById(user.getChatId()).get();
        groups = user.getGroups();
        groupController.delete(groups);

        user = userRepository.findById(user.getChatId()).get();
        feedbackController.markToDelete(user.getFeedbacks());

        user = userRepository.findById(user.getChatId()).get();
        userRepository.delete(user);
    }
}
