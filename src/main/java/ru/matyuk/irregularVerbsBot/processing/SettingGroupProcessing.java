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

import static ru.matyuk.irregularVerbsBot.design.Messages.CHOOSE_GROUP_FOR_DELETE_MESSAGE;


@Component
public class SettingGroupProcessing extends MainProcessing {

    public SettingGroupProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController, SessionController sessionController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController, sessionController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());

        switch (command){
            case CREATE_GROUP:
                return createGroup(user, messageId);
            case REMOVE_GROUP:
                return removeGroup(user, messageId);
            case BACK_TO_SETTING_MAIN:
                return settingMain(user, messageId);
        }
        return null;
    }

    @Override
    public Response processing(String messageText, User user) {
        return null;
    }

    private Response removeGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.DELETE_GROUP_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getGroupKeyboardForDelete(user);

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(CHOOSE_GROUP_FOR_DELETE_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

}
