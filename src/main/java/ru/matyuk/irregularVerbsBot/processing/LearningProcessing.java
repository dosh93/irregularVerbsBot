package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.Learning;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.processing.data.Response;

import java.util.Arrays;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;

@Component
public class LearningProcessing extends MainProcessing {

    public LearningProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command) {
            case START_LEARN:
                return learning(user, messageId);
            case BACK_TO_MAIN:
                return back(user, messageId);
            case END_LEARNING:
                return endLearning(user, messageId);
        }
        return null;
    }


    @Override
    public Response processing(String messageText, User user) {
        ReplyKeyboard replyKeyboard;
        List<String> verbsAnswer = Arrays.asList(messageText.split(" "));
        StringBuilder responseText = new StringBuilder();
        if(verbsAnswer.size() != 2){
            replyKeyboard = null;
            responseText.append(INVALID_RESPONSE_MESSAGE);
        }else {
            Learning learningVerb = learningController.getLearningActive(user);
            if(learningVerb != null){
                if(learningController.isValidAnswerUser(verbsAnswer, learningVerb)){
                    responseText.append(RIGHT_MESSAGE).append("\n\n");
                    learningController.setInactiveAndAddSuccessful(learningVerb);
                }else {
                    responseText.append(NOT_RIGHT_MESSAGE).append("\n")
                            .append(learningVerb.getVerb().toString()).append("\n\n");
                    learningController.resetCountSuccessful(learningVerb);
                    learningController.setInactive(learningVerb);
                }
                responseText.append(newLearningVerb(user));
                replyKeyboard = keyboard.getEndLearningButton();
            }else {
                replyKeyboard = null;
                responseText.append(INVALID_RESPONSE_MESSAGE);
            }
        }

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(null)
                .responseMessage(getResponseMessage(responseText.toString(), user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    private Response learning(User user, Integer messageId) {
        ReplyKeyboard replyKeyboard = keyboard.getEndLearningButton();

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(getDeleteMessage(messageId, user.getChatId()))
                .responseMessage(getResponseMessage(newLearningVerb(user), user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    private String newLearningVerb(User user){
        Verb learningVerb = learningController.getVerbForLearning(user);
        StringBuilder responseText = new StringBuilder();
        if(learningVerb == null)
            responseText.append(CONGRATULATION_MESSAGE);
        else
            responseText.append(String.format(WRITE_ANSWER_MESSAGE, learningVerb.getFirstForm(), learningVerb.getTranslate()));
        return responseText.toString();
    }

    private Response endLearning(User user, Integer messageId) {
        Learning learningVerb = learningController.getLearningActive(user);
        if(learningVerb != null)learningController.setInactive(learningVerb);
        user = userController.setState(user, StateUser.MAIN_MENU_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getMainMenu(user);
        String responseText = MAIN_MENU_MESSAGE + GOOD_WORK_MESSAGE;

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(getDeleteMessage(messageId, user.getChatId()))
                .responseMessage(getResponseMessage(responseText, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }
}
