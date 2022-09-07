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

import static ru.matyuk.irregularVerbsBot.design.Messages.*;

@Component
public class DeleteGroupProcessing extends MainProcessing {

    public DeleteGroupProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        Integer messageId = callbackQuery.getMessage().getMessageId();

        if(callbackQuery.getData().equals(ButtonCommand.BACK_TO_SETTING_GROUP.name())){
            return settingGroup(user, messageId);
        } else {
            return deleteGroup(user, messageId, callbackQuery.getData());
        }
    }

    @Override
    public Response processing(String messageText, User user) {
        return null;
    }
    private Response deleteGroup(User user, Integer messageId, String data) {
        user = userController.setState(user, StateUser.SETTING_GROUP_STATE);
        groupController.delete(Long.parseLong(data));

        ReplyKeyboard replyKeyboard = keyboard.getSettingGroupButton();
        String responseText = DELETE_GROUP_DONE_MESSAGE +
                "\n\n" + SETTING_GROUP_MESSAGE;

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(getDeleteMessage(messageId, user.getChatId()))
                .responseMessage(getResponseMessage(responseText, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }




}
