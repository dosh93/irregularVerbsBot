package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.model.Learning;
import ru.matyuk.irregularVerbsBot.model.Session;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.processing.data.Response;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;
import static ru.matyuk.irregularVerbsBot.design.Messages.INVALID_RESPONSE_MESSAGE;
import static ru.matyuk.irregularVerbsBot.utils.CommonUtils.getDeleteAudio;

@Component
public class ChallengeProcessing extends MainProcessing{

    public ChallengeProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController, SessionController sessionController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController, sessionController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case BACK_TO_MAIN:
                return back(user, messageId);
            case RANDOM_10:
                return startChallenge(user, messageId, 10);
            case RANDOM_20:
                return startChallenge(user, messageId, 20);
            case RANDOM_30:
                return startChallenge(user, messageId, 30);
            case RANDOM_40:
                return startChallenge(user, messageId, 40);
            case RANDOM_50:
                return startChallenge(user, messageId, 50);
            case RANDOM_100:
                return startChallenge(user, messageId, 100);
            case START_LEARN:
                return challenge(user, messageId);
            case BACK_TO_CHOSE_CHALLENGE:
                userController.stopSession(user);
                return startChallenge(user, messageId);
            case END_LEARNING:
                return endChallenge(user,messageId);
        }
        return null;
    }

    private Response endChallenge(User user, Integer messageId) {
        Session activeSession = user.getActiveSession();

        activeSession.setState(false);
        activeSession.setStop(new Timestamp(System.currentTimeMillis()));
        String responseText = getStatisticMessage(activeSession);
        ReplyKeyboard replyKeyboard = keyboard.getStatisticsButton();
        user = userController.save(user);

        List<DeleteMessage> deleteMessages = new ArrayList<>();
        if(messageId != null) deleteMessages.add(getDeleteMessage(messageId, user.getChatId()));
        deleteMessages.add(getDeleteAudio(user));

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(deleteMessages)
                .responseMessage(getResponseMessage(responseText, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

    @Override
    public Response processing(String messageText, User user) {
        ReplyKeyboard replyKeyboard;
        List<String> verbsAnswer = Arrays.asList(messageText.split(" "));
        StringBuilder responseText = new StringBuilder();
        File file = null;
        String nameAudio = null;

        if (verbsAnswer.size() != 2) {
            replyKeyboard = null;
            responseText.append(INVALID_RESPONSE_MESSAGE);
        } else {
            Verb learningVerb = sessionController.getVerbActive(user);

            if (learningVerb != null) {
                file = verbController.getAudioFile(learningVerb, user);
                if(file != null) nameAudio = learningVerb.getFirstForm();

                if(learningController.isValidAnswerUser(verbsAnswer, learningVerb)){
                    responseText.append(validAnswer(user, learningVerb, verbsAnswer));
                } else {
                    responseText.append(invalidAnswer(user, learningVerb));
                }

                Session activeSession = user.getActiveSession();
                int left = activeSession.getCount() - (activeSession.getFail() +  activeSession.getSuccess());
                Verb nextVerb = userController.getNextVerbForChallenge(user);

                if(nextVerb == null){
                    return endChallenge(user, null);
                } else {
                    String responseMessage = String.format(WRITE_ANSWER_CHALLENGE_MESSAGE, activeSession.getCount(), left, nextVerb.getFirstForm(), nextVerb.getTranslate());
                    responseText.append(responseMessage);
                    replyKeyboard = keyboard.getEndLearningButton();
                }
            } else {
                replyKeyboard = null;
                responseText.append(INVALID_RESPONSE_MESSAGE);
            }
        }

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(null)
                .responseMessage(getResponseMessage(responseText.toString(), user.getChatId(), replyKeyboard, file, nameAudio))
                .user(user)
                .build();
    }

    private Response startChallenge(User user, Integer messageId, int count) {
        sessionController.createSessionChallenge(user, count);

        ReplyKeyboard replyKeyboard = keyboard.getStartChallengingButton();

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(String.format(INSTRUCTION_CHALLENGE_MESSAGE_FORMAT, count), user.getChatId(), replyKeyboard))
                .user(user)
                .build();

    }

    private Response challenge(User user, Integer messageId) {
        Session activeSession = user.getActiveSession();
        Verb learningVerb = userController.getNextVerbForChallenge(user);

        ReplyKeyboard replyKeyboard = keyboard.getEndLearningButton();

        String responseMessage = String.format(WRITE_ANSWER_CHALLENGE_MESSAGE, activeSession.getCount(), activeSession.getCount(), learningVerb.getFirstForm(), learningVerb.getTranslate());
        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(responseMessage, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }
}
