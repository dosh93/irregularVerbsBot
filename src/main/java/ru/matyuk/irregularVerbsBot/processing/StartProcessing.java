package ru.matyuk.irregularVerbsBot.processing;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.Response;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;


@Slf4j
@Component
public class StartProcessing extends MainProcessing {

    public StartProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController, SessionController sessionController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController, sessionController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        return null;
    }

    @Override
    public Response processing(String messageText, User user) {
        String responseText = MAIN_MENU_MESSAGE + String.format(HELLO_MESSAGE, user.getFirstName());
        long chatId = user.getChatId();

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(null)
                .responseMessage(getResponseMessage(responseText, chatId, keyboard.getMainMenu(user)))
                .user(user)
                .build();
    }
}
