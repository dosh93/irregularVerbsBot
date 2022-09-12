package ru.matyuk.irregularVerbsBot.processing;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;
import static ru.matyuk.irregularVerbsBot.utils.CommonUtils.getDeleteAudio;

@Component
@Slf4j
public class LearningProcessing extends MainProcessing {

    final ResourceLoader resourceLoader;
    public LearningProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController, ResourceLoader resourceLoader) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController);
        this.resourceLoader = resourceLoader;
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
        File file = null;
        String nameAudio = null;
        if(verbsAnswer.size() != 2){
            replyKeyboard = null;
            responseText.append(INVALID_RESPONSE_MESSAGE);
        }else {
            Learning learningVerb = learningController.getLearningActive(user);

            if(learningVerb != null){
                if(learningController.isValidAnswerUser(verbsAnswer, learningVerb)){
                    if(learningVerb.getVerb().getAudio() != null && user.isViewAudio()){
                        try {
                            file = resourceLoader.getResource("classpath:" + learningVerb.getVerb().getAudio()).getFile();
                            nameAudio = learningVerb.getVerb().getFirstForm();
                        } catch (IOException e) {
                            log.error("Файл не найден " + e.getMessage());
                        }
                    }
                    responseText.append(RIGHT_MESSAGE).append("\n").append(getAdvice(learningVerb, verbsAnswer));
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
                .responseMessage(getResponseMessage(responseText.toString(), user.getChatId(), replyKeyboard, file, nameAudio))
                .user(user)
                .build();
    }

    private String getAdvice(Learning learningVerb, List<String> verbsAnswer) {
        StringBuilder result = new StringBuilder();
        Verb verb = learningVerb.getVerb();
        result.append(MORE_VARIANT_VERB_MESSAGE).append("\n")
                .append(verb.getFirstForm()).append(" - ");
        String secondFormAnswer = verbsAnswer.get(0);
        String thirdFormAnswer = verbsAnswer.get(1);
        if (verb.getSecondForm().split("/").length > 1) {
            if (secondFormAnswer.split("/").length > 1) result.append(verb.getSecondForm()).append(" - ");
            else {
                String[] split = verb.getSecondForm().split("/");
                for (String str : split) {
                    if (!str.equals(secondFormAnswer)){
                        result.append(str).append(" - ");
                        break;
                    }
                }
            }
        } else result.append(verb.getSecondForm()).append(" - ");

        if (verb.getThirdForm().split("/").length > 1) {
            if (thirdFormAnswer.split("/").length > 1) result.append(verb.getThirdForm());
            else {
                String[] split = verb.getThirdForm().split("/");
                for (String str : split) {
                    if (!str.equals(thirdFormAnswer)){
                        result.append(str);
                        break;
                    }
                }
            }
        } else result.append(verb.getThirdForm());
        return result.append("\n\n").toString();
    }

    private Response learning(User user, Integer messageId) {
        ReplyKeyboard replyKeyboard = keyboard.getEndLearningButton();

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
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

        List<DeleteMessage> deleteMessages = new ArrayList<>();
        deleteMessages.add(getDeleteMessage(messageId, user.getChatId()));
        deleteMessages.add(getDeleteAudio(user));

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(deleteMessages)
                .responseMessage(getResponseMessage(responseText, user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }
}
