package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.Response;

import java.util.ArrayList;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;

@Component
public class SettingLearningProcessing extends MainProcessing {

    public SettingLearningProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController, SessionController sessionController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController, sessionController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case RESET_LEARNING_ALL:
                return resetLearningAll(user, messageId);
            case RESET_LEARNING_GROUP:
                return resetLearningGroup(user, messageId);
            case BACK_TO_MAIN:
                return back(user, messageId);
        }
        return null;
    }

    private Response resetLearningGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.CHOOSE_GROUP_RESET_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getGroupForReset(user);

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(RESET_LEARNING_FRO_GROUP_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    private Response resetLearningAll(User user, Integer messageId) {
        user = userController.setState(user, StateUser.MAIN_MENU_STATE);

        learningController.delete(user.getLearnings());
        userGroupLearningController.delete(user.getGroupLearnings());

        user = userController.getUser(user.getChatId());

        String responseMessage = MAIN_MENU_MESSAGE + RESET_LEARNING_ALL_MESSAGE;
        ReplyKeyboard replyKeyboard = keyboard.getMainMenu(user);

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(responseMessage, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    @Override
    public Response processing(String messageText, User user) {
        return null;
    }
}
