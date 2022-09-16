package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.design.Messages;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.Response;

import java.util.ArrayList;
import java.util.List;


@Component
public class MainMenuProcessing extends MainProcessing {

    public MainMenuProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController, SessionController sessionController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController, sessionController);
    }

    public Response processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case LEARNING:
                return learning(user, messageId);
            case SETTING_GROUP:
                return settingGroup(user, messageId);
            case CHOOSE_GROUP:
                return chooseGroup(user, messageId);
            case VIEW_GROUP:
                return viewGroup(user, messageId);
            case FEEDBACK:
                return feedback(user, messageId);
            case SETTING_LEARNING:
                return settingLearning(user, messageId);
            case SETTING_MAIN:
                return settingMain(user, messageId);
            case CHALLENGE:
                return startChallenge(user, messageId);
        }
        return null;
    }

    @Override
    public Response processing(String messageText, User user) {
        return null;
    }

    private Response learning(User user, Integer messageId) {
        user = userController.setState(user, StateUser.LEARNING_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getStartLearningButton();

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(Messages.INSTRUCTION_LEARN_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    private Response feedback(User user, Integer messageId) {
        user = userController.setState(user, StateUser.CREATE_FEEDBACK_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getFeedbackButtons();

        return Response.builder()
                .isSaveSentMessageId(true)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(Messages.TYPE_TEXT_FEEDBACK_MESSAGE, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

}
