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
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.UserGroupLearning;
import ru.matyuk.irregularVerbsBot.processing.data.Response;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;

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

    @Autowired
    public MainProcessing(Keyboard keyboard,
                          UserController userController,
                          GroupController groupController,
                          LearningController learningController,
                          VerbController verbController,
                          GroupVerbController groupVerbController,
                          FeedbackController feedbackController,
                          UserGroupLearningController userGroupLearningController
    ){
        this.keyboard = keyboard;
        this.userController = userController;
        this.groupController = groupController;
        this.learningController = learningController;
        this.verbController = verbController;
        this.groupVerbController = groupVerbController;
        this.feedbackController = feedbackController;
        this.userGroupLearningController = userGroupLearningController;
    }

    protected Gson gson = new Gson();

    public abstract Response processing(CallbackQuery callbackQuery, User user);
    public abstract Response processing(String messageText, User user) ;

    protected Response back(User user, Integer messageId) {
        user = userController.setState(user, StateUser.MAIN_MENU_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getMainMenu(user);

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(getDeleteMessage(messageId, user.getChatId()))
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
                .deleteMessage(getDeleteMessage(messageId, user.getChatId()))
                .responseMessage(getResponseMessage(responseMessage.toString(), user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    protected Response viewGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.VIEW_GROUP_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getGroupViewKeyboard(user);

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(getDeleteMessage(messageId, user.getChatId()))
                .responseMessage(getResponseMessage(CHOOSE_GROUP_FOR_VIEW_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    protected Response settingGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.SETTING_GROUP_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getSettingGroupButton();

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(getDeleteMessage(messageId, user.getChatId()))
                .responseMessage(getResponseMessage(Messages.SETTING_GROUP_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    protected Response createGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.CREATE_GROUP_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getCreateGroupButton();

        return Response.builder()
                .isSaveSentMessageId(true)
                .deleteMessage(getDeleteMessage(messageId, user.getChatId()))
                .responseMessage(getResponseMessage(INSTRUCTION_CREATE_GROUP_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    protected Response settingLearning(User user, Integer messageId) {
        user = userController.setState(user, StateUser.SETTING_LEARNING_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getSettingLearningButton();

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(getDeleteMessage(messageId, user.getChatId()))
                .responseMessage(getResponseMessage(SETTING_LEARNING_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    protected Response settingMain(User user, Integer messageId) {
        user = userController.setState(user, StateUser.SETTING_MAIN_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getSettingMain();

        String response = String.format(SETTING_MAIN_MESSAGE_FORMAT, user.getCountSuccessful());

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(getDeleteMessage(messageId, user.getChatId()))
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
}
