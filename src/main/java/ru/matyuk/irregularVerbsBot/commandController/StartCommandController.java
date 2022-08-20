package ru.matyuk.irregularVerbsBot.commandController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.matyuk.irregularVerbsBot.Keyboard;
import ru.matyuk.irregularVerbsBot.controller.GroupVerbController;
import ru.matyuk.irregularVerbsBot.controller.LearningController;
import ru.matyuk.irregularVerbsBot.controller.UserController;
import ru.matyuk.irregularVerbsBot.controller.VerbController;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.model.Learning;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StartCommandController {

    @Autowired
    private UserController userController;

    @Autowired
    private GroupVerbController groupVerbController;

    @Autowired
    private VerbController verbController;

    @Autowired
    private LearningController learningController;

    @Autowired
    private Keyboard keyboard;

    public ResponseMessage startCommand(Message message){

        String answer = "Привет, " + message.getChat().getFirstName() + " :relaxed: Давай учится!";
        User user = userController.registerUser(message);
        log.info(String.format("Ответ пользователю chatId = %d message = %s",  message.getChatId(), answer));

        return ResponseMessage.builder()
                .message(answer)
                .chatId(message.getChatId())
                .keyboard(keyboard.getReplyKeyboardMarkupByState(user.getState()))
                .build();
    }

    public ResponseMessage unknownCommand(Message message){

        String answer = "Не знаю такую команду :pensive:";
        User user = userController.getUser(message.getChatId());
        log.info(String.format("Ответ пользователю chatId = %d message = %s",  message.getChatId(), answer));

        return ResponseMessage.builder()
                .message(answer)
                .chatId(message.getChatId())
                .keyboard(keyboard.getReplyKeyboardMarkupByState(user.getState()))
                .build();
    }

    public ResponseMessage viewGroupVerb(Message message){
        return createMessageForViewGroup(StateUser.VIEW_GROUP, message);

    }
    public ResponseMessage chooseGroupVerb(Message message){
       return createMessageForViewGroup(StateUser.CHOOSE_GROUP, message);
    }

    public ResponseMessage createMessageForViewGroup(StateUser state, Message message){
        String answer = "Выбирете группу";
        User user = userController.setState(message.getChatId(), state);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());

        log.info(String.format("Ответ пользователю chatId = %d message = %s",  message.getChatId(), answer));

        return ResponseMessage.builder()
                .message(answer)
                .chatId(message.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }


    public ResponseMessage viewSelectedGroupVerb(Message message) {
        String groupName = message.getText();
        User user = userController.getUser(message.getChatId());
        String answer = null;
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        GroupVerb group = groupVerbController.getGroup(groupName);
        if(group == null){
            answer = "Нет такой группы";
            return ResponseMessage.builder()
                    .message(answer)
                    .chatId(message.getChatId())
                    .keyboard(replyKeyboardMarkupByState)
                    .build();
        }
        StateUser state =  learningController.getCountLearningByUser(user) > 0 ? StateUser.START_LEARN : StateUser.REGISTERED;
        user = userController.setState(message.getChatId(), state);
        replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        StringBuilder answerBuilder = new StringBuilder("Глаголы в группе:\n");
        List<Verb> verbsByGroup = verbController.getVerbsByGroupId(group);
        verbsByGroup.forEach(verb -> answerBuilder
                .append(String.format("%s - %s\n", verb.getFirstForm(), verb.getTranslate())));

        return ResponseMessage.builder()
                .message(answerBuilder.toString())
                .chatId(message.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage goToMainMenu(Message message){
        String answer = "Главное меню";
        User user = userController.getUser(message.getChatId());
        user = userController.setState(message.getChatId(), learningController.getCountLearningByUser(user) > 0 ? StateUser.START_LEARN : StateUser.REGISTERED);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        return ResponseMessage.builder()
                .message(answer)
                .chatId(message.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage startLearning(Message message){
        String groupName = message.getText();
        User user = userController.getUser(message.getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        GroupVerb group = groupVerbController.getGroup(groupName);
        String answer = null;

        if(group == null){
            answer = "Нет такой группы";
            return ResponseMessage.builder()
                    .message(answer)
                    .chatId(message.getChatId())
                    .keyboard(replyKeyboardMarkupByState)
                    .build();
        }
        answer = String.format("Выбрана группа для изучения: %s", groupName);
        user = userController.setState(message.getChatId(),StateUser.START_LEARN);
        replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        List<Verb> verbsByGroup = verbController.getVerbsByGroupId(group);
        learningController.addToLearning(user, verbsByGroup);
        return ResponseMessage.builder()
                .message(answer)
                .chatId(message.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage learning(Message message){
        String answer = null;
        User user = userController.getUser(message.getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        int countLearning = learningController.getCountLearningByUser(user);

        if(countLearning == 0){
            answer = "Нужно выбрать группу для изучения";
            return ResponseMessage.builder()
                    .message(answer)
                    .chatId(message.getChatId())
                    .keyboard(replyKeyboardMarkupByState)
                    .build();
        }
        Verb learningVerb = learningController.getVerbForLearning(user);
        if(user.getState() != StateUser.LEARNING_IN_PROCESS) user = userController.setState(user.getChatId(), StateUser.LEARNING_IN_PROCESS);
        replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());

        if(learningVerb == null)
            answer = "Поздравляю, вы выучили все слова из группы!\nНажмите \"Закончить\" и выберете новую группу слов";
        else
            answer = String.format("Напиши вторую и третью форму через пробел для:\n%s - %s",
                    learningVerb.getFirstForm(), learningVerb.getTranslate());
        return ResponseMessage.builder()
                .message(answer)
                .chatId(message.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage checkAnswer(Message message) {
        User user = userController.getUser(message.getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        String answer = null;
        List<String> answerUserList = Arrays.stream(message.getText().split(" ")).collect(Collectors.toList());
        if(answerUserList.size() != 2) answer = "Неверный формат ответа";
        else {
            Learning learningVerb = learningController.getLearningActive(user);
            if(learningController.isValidAnswerUser(answerUserList, learningVerb)){
                answer = "Верно!";
                learningController.setInactiveAndAddSuccessful(learningVerb);
            }else {
                answer = "Неверно! Попробую ещё раз";
            }
        }
        return ResponseMessage.builder()
                .message(answer)
                .chatId(message.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage stopLearning(Message message){
        User user = userController.getUser(message.getChatId());
        user = userController.setState(user.getChatId(), StateUser.START_LEARN);
        Learning learningActive = learningController.getLearningActive(user);
        if(learningActive != null) learningController.setInactive(learningActive);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        String answer = "Хорошо сегодня потрудились!";
        return ResponseMessage.builder()
                .message(answer)
                .chatId(message.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }
}
