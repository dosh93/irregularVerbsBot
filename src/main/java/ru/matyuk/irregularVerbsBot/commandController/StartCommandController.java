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
import ru.matyuk.irregularVerbsBot.model.Compilation;
import ru.matyuk.irregularVerbsBot.model.Learning;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.matyuk.irregularVerbsBot.config.Messages.*;

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

        String answer = String.format(HELLO_MESSAGE, message.getChat().getFirstName());
        User user = userController.registerUser(message);

        return ResponseMessage.builder()
                .message(answer)
                .chatId(message.getChatId())
                .keyboard(keyboard.getReplyKeyboardMarkupByState(user.getState()))
                .build();
    }

    public ResponseMessage unknownCommand(User user){
        return ResponseMessage.builder()
                .message(UNKNOWN_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(keyboard.getReplyKeyboardMarkupByState(user.getState()))
                .build();
    }

    public ResponseMessage viewGroupVerb(User user){
        return createMessageForViewGroup(StateUser.VIEW_GROUP, user);

    }
    public ResponseMessage chooseGroupVerb(User user){
       return createMessageForViewGroup(StateUser.CHOOSE_GROUP, user);
    }

    public ResponseMessage createMessageForViewGroup(StateUser state, User user){
        user = userController.setState(user, state);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());

        return ResponseMessage.builder()
                .message(CHOOSE_GROUP_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }


    public ResponseMessage viewSelectedGroupVerb(User user, String messageText) {
        Compilation group = groupVerbController.getGroup(messageText);

        ResponseMessage responseMessage = checkGroup(user, group);
        if(responseMessage != null) return  responseMessage;
        List<Verb> verbsByGroup = group.getVerbs();
        responseMessage = checkVerbInGroup(user, verbsByGroup);
        if(responseMessage != null) return  responseMessage;

        StateUser state =  userController.isLearning(user) ? StateUser.START_LEARN : StateUser.REGISTERED;
        user = userController.setState(user, state);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        StringBuilder answerBuilder = new StringBuilder(VERBS_IN_GROUP_MESSAGE).append("\n");
        verbsByGroup.forEach(verb -> answerBuilder.append(verb.toString()).append("\n"));

        return ResponseMessage.builder()
                .message(answerBuilder.toString())
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage goToMainMenu(User user){
        user = userController.setState(user, userController.isLearning(user) ? StateUser.START_LEARN : StateUser.REGISTERED);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        return ResponseMessage.builder()
                .message(MAIN_MENU_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage startLearning(User user, String groupName){
        Compilation group = groupVerbController.getGroup(groupName);

        ResponseMessage responseMessage = checkGroup(user, group);
        if(responseMessage != null) return  responseMessage;
        List<Verb> verbsByGroup = group.getVerbs();
        responseMessage = checkVerbInGroup(user, verbsByGroup);
        if(responseMessage != null) return  responseMessage;

        String answer = String.format(SELECTED_GROUP_MESSAGE, groupName);
        user = userController.setState(user, StateUser.START_LEARN);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        learningController.addToLearning(user, verbsByGroup);

        return ResponseMessage.builder()
                .message(answer)
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    private ResponseMessage checkGroup(User user, Compilation group){
        if(group == null){
            return ResponseMessage.builder()
                    .message(NO_GROUP_MESSAGE)
                    .chatId(user.getChatId())
                    .keyboard(keyboard.getReplyKeyboardMarkupByState(user.getState()))
                    .build();
        }
        return null;
    }
    private ResponseMessage checkVerbInGroup(User user, List<Verb> verbs){
        if(verbs.size() == 0){
            return ResponseMessage.builder()
                    .message(GROUP_IS_EMPTY_MESSAGE)
                    .chatId(user.getChatId())
                    .keyboard(keyboard.getReplyKeyboardMarkupByState(user.getState()))
                    .build();
        }
        return null;
    }

    public ResponseMessage learning(User user){
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());

        if(!userController.isLearning(user)){
            return ResponseMessage.builder()
                    .message(NEED_CHOOSE_GROUP_MESSAGE)
                    .chatId(user.getChatId())
                    .keyboard(replyKeyboardMarkupByState)
                    .build();
        }

        Verb learningVerb = learningController.getVerbForLearning(user);
        if(user.getState() != StateUser.LEARNING_IN_PROCESS) user = userController.setState(user, StateUser.LEARNING_IN_PROCESS);
        replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());

        String answer;
        if(learningVerb == null)
            answer = CONGRATULATION_MESSAGE;
        else
            answer = String.format(WRITE_ANSWER_MESSAGE, learningVerb.getFirstForm(), learningVerb.getTranslate());
        return ResponseMessage.builder()
                .message(answer)
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage checkAnswer(User user, String messageText) {
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        List<String> answerUserList = Arrays.stream(messageText.split(" ")).collect(Collectors.toList());
        String answer;

        if(answerUserList.size() != 2) answer = INVALID_RESPONSE_MESSAGE;
        else {
            Learning learningVerb = learningController.getLearningActive(user);
            if(learningController.isValidAnswerUser(answerUserList, learningVerb)){
                answer = RIGHT_MESSAGE;
                learningController.setInactiveAndAddSuccessful(learningVerb);
            }else {
                answer = NOT_RIGHT_MESSAGE + "\n" + learningVerb.getVerb().toString();
                learningController.resetCountSuccessful(learningVerb);
            }
        }
        return ResponseMessage.builder()
                .message(answer)
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage stopLearning(User user){
        user = userController.setState(user, StateUser.START_LEARN);
        Learning learningActive = learningController.getLearningActive(user);
        if(learningActive != null) learningController.setInactive(learningActive);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState());
        return ResponseMessage.builder()
                .message(GOOD_WORK_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }
}
