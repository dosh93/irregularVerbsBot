package ru.matyuk.irregularVerbsBot.processing;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.design.Messages;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Smiles;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.UserGroupLearning;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import java.util.List;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;

public abstract class MainProcessing {

    protected TelegramBot telegramBot;

    @Autowired
    protected Keyboard keyboard;
    @Autowired
    protected UserController userController;

    @Autowired
    protected GroupController groupController;

    @Autowired
    protected LearningController learningController;

    @Autowired
    protected VerbController verbController;

    @Autowired
    protected GroupVerbController groupVerbController;
    @Autowired
    protected FeedbackController feedbackController;

    @Autowired
    protected UserGroupLearningController userGroupLearningController;

    protected Gson gson = new Gson();

    public MainProcessing(@Lazy TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public abstract void processing(CallbackQuery callbackQuery, User user);
    public abstract void processing(String messageText, User user) ;

    protected void back(User user, Integer messageId) {
        user = userController.setState(user, StateUser.MAIN_MENU_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getMainMenu(user);

        ResponseMessage response = ResponseMessage.builder()
                .message(Messages.MAIN_MENU_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }

    protected void chooseGroup(User user, Integer messageId) {
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
        ResponseMessage response = ResponseMessage.builder()
                .message(responseMessage.toString())
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }

    protected void viewGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.VIEW_GROUP_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getGroupViewKeyboard(user);

        ResponseMessage response = ResponseMessage.builder()
                .message(CHOOSE_GROUP_FOR_VIEW_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }

    protected void settingGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.SETTING_GROUP_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getSettingGroupButton();

        ResponseMessage response = ResponseMessage.builder()
                .message(Messages.SETTING_GROUP_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }

    protected void createGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.CREATE_GROUP_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getCreateGroupButton();

        ResponseMessage response = ResponseMessage.builder()
                .message(INSTRUCTION_CREATE_GROUP_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        userController.setTmp(user, telegramBot.sendMessage(response).toString());
    }

    protected void settingLearning(User user, Integer messageId) {
        user = userController.setState(user, StateUser.SETTING_LEARNING_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getSettingLearningButton();

        ResponseMessage response = ResponseMessage.builder()
                .message(SETTING_LEARNING_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }
}
