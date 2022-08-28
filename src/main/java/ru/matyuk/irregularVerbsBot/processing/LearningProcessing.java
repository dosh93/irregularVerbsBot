package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.Learning;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import java.util.Arrays;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;

@Component
public class LearningProcessing extends MainProcessing{

    public LearningProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public void processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command) {
            case START_LEARN:
                learning(user, messageId);
                break;
            case BACK_TO_MAIN:
                back(user, messageId);
                break;
            case END_LEARNING:
                endLearning(user, messageId);
                break;
        }
    }


    @Override
    public void processing(String messageText, User user) {
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

        ResponseMessage response = ResponseMessage.builder()
                .message(responseText.toString())
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.sendMessage(response);
    }

    private void learning(User user, Integer messageId) {
        ReplyKeyboard replyKeyboard = keyboard.getEndLearningButton();

        ResponseMessage response = ResponseMessage.builder()
                .message(newLearningVerb(user))
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
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

    private void endLearning(User user, Integer messageId) {
        Learning learningVerb = learningController.getLearningActive(user);
        if(learningVerb != null)learningController.setInactive(learningVerb);
        user = userController.setState(user, StateUser.MAIN_MENU_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getMainMenu(user);
        String responseText = GOOD_WORK_MESSAGE + "\n\n" + MAIN_MENU_MESSAGE;

        ResponseMessage response = ResponseMessage.builder()
                .message(responseText)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }
}
