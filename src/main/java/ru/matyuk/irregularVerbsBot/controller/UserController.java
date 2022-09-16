package ru.matyuk.irregularVerbsBot.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.jsonPojo.ChallengeVerbPojo;
import ru.matyuk.irregularVerbsBot.jsonPojo.CreateGroupPojo;
import ru.matyuk.irregularVerbsBot.model.*;
import ru.matyuk.irregularVerbsBot.model.id.UserGroupId;
import ru.matyuk.irregularVerbsBot.model.id.UserVerbId;
import ru.matyuk.irregularVerbsBot.repository.UserRepository;
import ru.matyuk.irregularVerbsBot.utils.CommonUtils;

import java.sql.Timestamp;
import java.util.*;

@Component
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final GroupController groupController;
    private final LearningController learningController;
    private final GroupVerbController groupVerbController;
    private final FeedbackController feedbackController;
    private final UserGroupLearningController userGroupLearningController;
    private final VerbController verbController;

    @Value("${learning.count_successful}")
    Integer countSuccessful;

    public UserController(UserRepository userRepository, GroupController groupController, LearningController learningController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController, VerbController verbController) {
        this.userRepository = userRepository;
        this.groupController = groupController;
        this.learningController = learningController;
        this.groupVerbController = groupVerbController;
        this.feedbackController = feedbackController;
        this.userGroupLearningController = userGroupLearningController;
        this.verbController = verbController;
    }


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
            user.setViewAudio(true);

            user = userRepository.save(user);
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
        return user.getGroupLearnings().size() != 0;
    }

    public User setTmp(User user, String tmp){
        user.setTmp(tmp);
        return userRepository.save(user);
    }

    public User saveLearning(User user, List<Verb> verbs, Group group){
        List<Learning> userLearning = user.getLearnings();
        List<UserGroupLearning> userGroupLearningList = user.getGroupLearnings();

        verbs.forEach(verb -> {
            UserVerbId userVerbId = new UserVerbId(user.getChatId(), verb.getId());
            if(learningController.getLearning(userVerbId) == null){
                Learning learning = new Learning();
                learning.setUser(user);
                learning.setVerb(verb);
                learning.setId(userVerbId);
                userLearning.add(learning);
            }
        });

        UserGroupLearning userGroupLearning = new UserGroupLearning();
        userGroupLearning.setUser(user);
        userGroupLearning.setGroup(group);
        userGroupLearning.setId(new UserGroupId(user.getChatId(), group.getId()));

        userGroupLearningList.add(userGroupLearning);

        user.setLearnings(userLearning);
        user.setGroupLearnings(userGroupLearningList);
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
        userGroupLearningController.delete(user.getGroupLearnings());

        user = userRepository.findById(user.getChatId()).get();
        groups = user.getGroups();
        groupController.delete(groups);

        user = userRepository.findById(user.getChatId()).get();
        feedbackController.markToDelete(user.getFeedbacks());

        user = userRepository.findById(user.getChatId()).get();
        userRepository.delete(user);
    }

    public User setCountSuccessful(int count, User user) {
        user.setCountSuccessful(count);
        return userRepository.save(user);
    }

    public User switchAudio(User user) {
        user.setViewAudio(!user.isViewAudio());
        return userRepository.save(user);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public void stopSession(User user) {
        Session activeSession = user.getActiveSession();
        if(activeSession != null) activeSession.setState(false);
        save(user);
    }

    public Verb getNextVerbForChallenge(User user) {
        Gson gson = new Gson();

        Session activeSession = user.getActiveSession();
        ChallengeVerbPojo challengeVerbPojo = gson.fromJson(activeSession.getJson(), ChallengeVerbPojo.class);

        if(challengeVerbPojo.getVerbIds().size() == activeSession.getCount()) return null;
        long count = verbController.getCount();
        long newId = -1;
        while (newId  == -1){
            int randomNumber = CommonUtils.getRandomNumber(1, (int) count);
            if(challengeVerbPojo.getVerbIds().contains((long) randomNumber)) continue;
            newId = randomNumber;
        }

        Verb verbById = verbController.getVerbById(newId);

        challengeVerbPojo.getVerbIds().add(newId);
        activeSession.setJson(gson.toJson(challengeVerbPojo));
        save(user);

        return verbById;
    }
}
