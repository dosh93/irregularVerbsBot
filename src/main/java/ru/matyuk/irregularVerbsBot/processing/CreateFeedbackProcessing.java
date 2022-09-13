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

import static ru.matyuk.irregularVerbsBot.design.Messages.FEEDBACK_CREATED_MESSAGE;
import static ru.matyuk.irregularVerbsBot.design.Messages.MAIN_MENU_MESSAGE;

@Component
public class CreateFeedbackProcessing extends MainProcessing {

    public CreateFeedbackProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController, SessionController sessionController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController, sessionController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case BACK_TO_MAIN:
                return back(user, messageId);
        }
        return null;
    }

    @Override
    public Response processing(String messageText, User user) {
        user = userController.setState(user, StateUser.MAIN_MENU_STATE);
        feedbackController.create(user, messageText);

        ReplyKeyboard replyKeyboard = keyboard.getMainMenu(user);
        String responseText = MAIN_MENU_MESSAGE + "\n\n" + FEEDBACK_CREATED_MESSAGE;

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(Integer.parseInt(user.getTmp()), user.getChatId()))))
                .responseMessage(getResponseMessage(responseText, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }
}
