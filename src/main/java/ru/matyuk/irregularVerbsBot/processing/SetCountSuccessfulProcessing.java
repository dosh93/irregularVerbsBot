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
public class SetCountSuccessfulProcessing extends MainProcessing{

    public SetCountSuccessfulProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController, SessionController sessionController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController, sessionController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case BACK_TO_SETTING_MAIN:
                return settingMain(user, messageId);
        }
        return null;
    }

    @Override
    public Response processing(String messageText, User user) {
        try {
            int count = Integer.parseInt(messageText);
            if (count < 1) {
                return Response.builder()
                        .isSaveSentMessageId(false)
                        .deleteMessage(null)
                        .responseMessage(getResponseMessage(INVALID_RESPONSE_MESSAGE, user.getChatId(), null))
                        .user(user)
                        .build();
            } else {
                user = userController.setCountSuccessful(count, user);
                user = userController.setState(user, StateUser.SETTING_MAIN_STATE);

                ReplyKeyboard replyKeyboard = keyboard.getSettingMain(user);

                String response = String.format(SETTING_MAIN_MESSAGE_FORMAT, user.getCountSuccessful()) +
                        SET_DONE_COUNT_SUCCESSFUL_MESSAGE;

                return Response.builder()
                        .isSaveSentMessageId(false)
                        .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(Integer.parseInt(user.getTmp()), user.getChatId()))))
                        .responseMessage(getResponseMessage(response, user.getChatId(), replyKeyboard))
                        .build();
            }
        } catch (NumberFormatException e){
            return Response.builder()
                    .isSaveSentMessageId(false)
                    .deleteMessage(null)
                    .responseMessage(getResponseMessage(INVALID_RESPONSE_MESSAGE, user.getChatId(), null))
                    .user(user)
                    .build();
        }
    }
}
