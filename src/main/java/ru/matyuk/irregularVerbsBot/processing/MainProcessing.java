package ru.matyuk.irregularVerbsBot.processing;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.design.Messages;
import ru.matyuk.irregularVerbsBot.design.Smiles;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.*;
import ru.matyuk.irregularVerbsBot.processing.data.Response;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;

public abstract class MainProcessing {

    protected Keyboard keyboard;
    protected UserController userController;
    protected GroupController groupController;
    protected LearningController learningController;
    protected VerbController verbController;
    protected GroupVerbController groupVerbController;
    protected FeedbackController feedbackController;
    protected UserGroupLearningController userGroupLearningController;
    protected SessionController sessionController;

    public MainProcessing(Keyboard keyboard,
                          UserController userController,
                          GroupController groupController,
                          LearningController learningController,
                          VerbController verbController,
                          GroupVerbController groupVerbController,
                          FeedbackController feedbackController,
                          UserGroupLearningController userGroupLearningController,
                          SessionController sessionController
    ){
        this.keyboard = keyboard;
        this.userController = userController;
        this.groupController = groupController;
        this.learningController = learningController;
        this.verbController = verbController;
        this.groupVerbController = groupVerbController;
        this.feedbackController = feedbackController;
        this.userGroupLearningController = userGroupLearningController;
        this.sessionController = sessionController;
    }

    protected Gson gson = new Gson();

    public abstract Response processing(CallbackQuery callbackQuery, User user);
    public abstract Response processing(String messageText, User user) ;

    protected Response back(User user, Integer messageId) {
        user = userController.setState(user, StateUser.MAIN_MENU_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getMainMenu(user);

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(Messages.MAIN_MENU_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    protected Response chooseGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.CHOOSE_GROUP_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getGroupChoseKeyboard(user);

        StringBuilder responseMessage = new StringBuilder(CHOOSE_GROUP_FOR_LEARNING_MESSAGE);
        List<UserGroupLearning> groupLearnings = user.getGroupLearnings();

        if(groupLearnings.size() > 0){
            responseMessage.append(Messages.SELECTED_GROUP_MESSAGE);
        }

        if(groupLearnings.size() > 0){
            groupLearnings.forEach(groupLearning -> responseMessage.append(Smiles.MINUS).append(" ")
                    .append(groupLearning.getGroup().getName()).append("\n"));
        }else {
            responseMessage.append(NO_SELECTED_GROUP_MESSAGE);
        }

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(responseMessage.toString(), user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    protected Response viewGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.VIEW_GROUP_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getGroupViewKeyboard(user);

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(CHOOSE_GROUP_FOR_VIEW_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    protected Response settingGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.SETTING_GROUP_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getSettingGroupButton();

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(Messages.SETTING_GROUP_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    protected Response createGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.CREATE_GROUP_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getCreateGroupButton();

        return Response.builder()
                .isSaveSentMessageId(true)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(INSTRUCTION_CREATE_GROUP_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    protected Response settingLearning(User user, Integer messageId) {
        user = userController.setState(user, StateUser.SETTING_LEARNING_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getSettingLearningButton();

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(SETTING_LEARNING_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    protected Response startChallenge(User user, Integer messageId) {
        user = userController.setState(user, StateUser.CHALLENGE_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getChallengeButtons();

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(Messages.START_CHALLENGE_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    protected String getAdvice(Verb verb, List<String> verbsAnswer) {
        StringBuilder result = new StringBuilder();
        if(verb.getSecondForm().split("/").length == 1 &&
                verb.getThirdForm().split("/").length == 1)
            return "";
        result.append(MORE_VARIANT_VERB_MESSAGE).append("\n")
                .append(verb.getFirstForm()).append(" - ");
        String secondFormAnswer = verbsAnswer.get(0);
        String thirdFormAnswer = verbsAnswer.get(1);
        if (verb.getSecondForm().split("/").length > 1) {
            if (secondFormAnswer.split("/").length > 1) result.append(verb.getSecondForm()).append(" - ");
            else {
                String[] split = verb.getSecondForm().split("/");
                for (String str : split) {
                    if (!str.equals(secondFormAnswer)){
                        result.append(str).append(" - ");
                        break;
                    }
                }
            }
        } else result.append(verb.getSecondForm()).append(" - ");

        if (verb.getThirdForm().split("/").length > 1) {
            if (thirdFormAnswer.split("/").length > 1) result.append(verb.getThirdForm());
            else {
                String[] split = verb.getThirdForm().split("/");
                for (String str : split) {
                    if (!str.equals(thirdFormAnswer)){
                        result.append(str);
                        break;
                    }
                }
            }
        } else result.append(verb.getThirdForm());
        return result.append("\n\n").toString();
    }

    protected String validAnswer(User user, Verb learningVerb, List<String> verbsAnswer){
        Session activeSession = user.getActiveSession();
        if (activeSession == null) {
            Session session = sessionController.createSession(user);
            session.setSuccess(1);
            user.getSessions().add(session);
            userController.save(user);
        } else {
            user.getActiveSession().setSuccess(user.getActiveSession().getSuccess() + 1);
            userController.save(user);
        }
        return RIGHT_MESSAGE + "\n" + getAdvice(learningVerb, verbsAnswer);
    }

    protected String invalidAnswer(User user, Verb learningVerb){
        Session activeSession = user.getActiveSession();
        if (activeSession == null) {
            Session session = sessionController.createSession(user);
            session.setFail(1);
            user.getSessions().add(session);
            userController.save(user);
        } else {
            user.getActiveSession().setFail(user.getActiveSession().getFail() + 1);
            userController.save(user);
        }
        return NOT_RIGHT_MESSAGE + "\n" + learningVerb + "\n\n";
    }

    protected Response settingMain(User user, Integer messageId) {
        user = userController.setState(user, StateUser.SETTING_MAIN_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getSettingMain(user);

        String response = String.format(SETTING_MAIN_MESSAGE_FORMAT, user.getCountSuccessful());

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(response, user.getChatId(), replyKeyboard))
                .build();
    }

    protected DeleteMessage getDeleteMessage(Integer messageId, Long chatId){
        return DeleteMessage.builder()
                .messageId(messageId)
                .chatId(String.valueOf(chatId))
                .build();
    }

    protected ResponseMessage getResponseMessage(String message, Long chatId, ReplyKeyboard keyboard){
        return ResponseMessage.builder()
                .message(message)
                .chatId(chatId)
                .keyboard(keyboard).build();
    }

    protected ResponseMessage getResponseMessage(String message, Long chatId, ReplyKeyboard keyboard, File audio, String audioName){
        return ResponseMessage.builder()
                .message(message)
                .chatId(chatId)
                .audio(audio)
                .audioName(audioName)
                .keyboard(keyboard).build();
    }

    protected String getStatisticMessage(Session session) {
        int fail = session.getFail();
        int success = session.getSuccess();
        long diff = session.getStop().getTime() - session.getStart().getTime();
        int secondAll = (int) (diff / 1000);
        int min = secondAll / 60;
        int second = min == 0 ? secondAll : secondAll - min * 60;
        return String.format(STATISTICS_MESSAGE_FORMAT, min, second, success, fail);
    }
}
