package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.design.Messages;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.Response;

import java.util.ArrayList;
import java.util.List;

@Component
public class SettingMainProcessing extends MainProcessing {

    public SettingMainProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case BACK_TO_MAIN:
                return back(user, messageId);
            case SETTING_GROUP:
                return settingGroup(user, messageId);
            case SET_COUNT_SUCCESSFUL:
                return startSetCountSuccessful(user, messageId);
            case SWITCH_AUDIO:
                return switchAudio(user, messageId);
        }
        return null;
    }

    private Response switchAudio(User user, Integer messageId) {
        user = userController.switchAudio(user);
        return settingMain(user, messageId);
    }

    private Response startSetCountSuccessful(User user, Integer messageId) {
        user = userController.setState(user, StateUser.SET_COUNT_SUCCESSFUL_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getSetCountSuccessful();

        return Response.builder()
                .isSaveSentMessageId(true)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(Messages.TYPE_NUMERICAL_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    @Override
    public Response processing(String messageText, User user) {
        return null;
    }
}
