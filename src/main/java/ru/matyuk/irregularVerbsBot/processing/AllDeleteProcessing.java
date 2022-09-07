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

import static ru.matyuk.irregularVerbsBot.design.Messages.ARE_YOU_SURE_DELETE_ALL_DATA_MESSAGE;
import static ru.matyuk.irregularVerbsBot.design.Messages.DELETE_ALL_DATA_MESSAGE;

@Component
public class AllDeleteProcessing extends MainProcessing {

    public AllDeleteProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case ALL_DELETE:
                return allDelete(user, messageId);
            case CANCEL_CONFIRM:
                return back(user, messageId);
        }
        return null;
    }

    @Override
    public Response processing(String messageText, User user) {
        user = userController.setState(user, StateUser.ALL_DELETE_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getAllDeleteButton();

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(null)
                .responseMessage(getResponseMessage(ARE_YOU_SURE_DELETE_ALL_DATA_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    private Response allDelete(User user, Integer messageId) {
        userController.delete(user);

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(getDeleteMessage(messageId, user.getChatId()))
                .responseMessage(getResponseMessage(DELETE_ALL_DATA_MESSAGE, user.getChatId(), null))
                .user(user)
                .build();
    }
}
