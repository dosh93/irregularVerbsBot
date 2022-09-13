package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.Response;

import java.util.ArrayList;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.design.Messages.GROUP_DONE_MESSAGE;
import static ru.matyuk.irregularVerbsBot.design.Messages.MAIN_MENU_MESSAGE;
import static ru.matyuk.irregularVerbsBot.enums.StateUser.MAIN_MENU_STATE;

@Component
public class SetNameGroupProcessing extends MainProcessing {

    public SetNameGroupProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController, SessionController sessionController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController, sessionController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        return null;
    }

    @Override
    public Response processing(String messageText, User user) {
        user = userController.setState(user, MAIN_MENU_STATE);
        Group group = groupController.getGroup(user.getChatId().toString());
        groupController.setName(group, messageText);
        String responseText =  MAIN_MENU_MESSAGE + String.format(GROUP_DONE_MESSAGE, messageText);
        ReplyKeyboard replyKeyboard = keyboard.getMainMenu(user);

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(Integer.parseInt(user.getTmp()), user.getChatId()))))
                .responseMessage(getResponseMessage(responseText, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }
}
