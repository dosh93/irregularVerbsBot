package ru.matyuk.irregularVerbsBot.commandController;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import ru.matyuk.irregularVerbsBot.Keyboard;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.jsonPojo.CreateGroupPojo;
import ru.matyuk.irregularVerbsBot.model.Compilation;
import ru.matyuk.irregularVerbsBot.model.Learning;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;

import java.util.*;
import java.util.stream.Collectors;

import static ru.matyuk.irregularVerbsBot.config.Messages.*;
import static ru.matyuk.irregularVerbsBot.enums.StateUser.DELETE_GROUP_STATE;

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
    private CompilationVerbController compilationVerbController;

    @Autowired
    private Keyboard keyboard;

    private final Gson gson = new Gson();;


    public ResponseMessage startCommand(Chat chat){

        String answer = String.format(HELLO_MESSAGE, chat.getFirstName());
        User user = userController.registerUser(chat);
        long chatId = chat.getId();

        return ResponseMessage.builder()
                .message(answer)
                .chatId(chatId)
                .keyboard(keyboard.getReplyKeyboardMarkupByState(user.getState(), chatId))
                .build();
    }

    public ResponseMessage unknownCommand(User user){
        return ResponseMessage.builder()
                .message(UNKNOWN_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId()))
                .build();
    }

    public ResponseMessage unknownCommandNotUser(long chatId){
        return ResponseMessage.builder()
                .message(UNKNOWN_MESSAGE)
                .chatId(chatId)
                .keyboard(null)
                .build();
    }

    public ResponseMessage viewGroupVerb(User user){
        return createMessageForViewGroup(StateUser.VIEW_GROUP_STATE, user);

    }
    public ResponseMessage chooseGroupVerb(User user){
       return createMessageForViewGroup(StateUser.CHOOSE_GROUP_STATE, user);
    }

    public ResponseMessage createMessageForViewGroup(StateUser state, User user){
        user = userController.setState(user, state);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());

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
        List<Verb> verbsByGroup = verbController.getVerbsByGroup(group);
        responseMessage = checkVerbInGroup(user, verbsByGroup);
        if(responseMessage != null) return  responseMessage;

        StateUser state =  userController.isLearning(user) ? StateUser.START_LEARN_STATE : StateUser.REGISTERED_STATE;
        user = userController.setState(user, state);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());
        StringBuilder answerBuilder = new StringBuilder(VERBS_IN_GROUP_MESSAGE).append("\n");
        verbsByGroup.forEach(verb -> answerBuilder.append(verb.toString()).append("\n"));

        return ResponseMessage.builder()
                .message(answerBuilder.toString())
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage goToMainMenu(User user){
        user = userController.setState(user, userController.isLearning(user) ? StateUser.START_LEARN_STATE : StateUser.REGISTERED_STATE);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());
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
        List<Verb> verbsByGroup = verbController.getVerbsByGroup(group);
        responseMessage = checkVerbInGroup(user, verbsByGroup);
        if(responseMessage != null) return  responseMessage;

        String answer = String.format(SELECTED_GROUP_MESSAGE, groupName);
        user = userController.setState(user, StateUser.START_LEARN_STATE);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());
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
                    .keyboard(keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId()))
                    .build();
        }
        return null;
    }
    private ResponseMessage checkVerbInGroup(User user, List<Verb> verbs){
        if(verbs.size() == 0){
            return ResponseMessage.builder()
                    .message(GROUP_IS_EMPTY_MESSAGE)
                    .chatId(user.getChatId())
                    .keyboard(keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId()))
                    .build();
        }
        return null;
    }

    public ResponseMessage learning(User user){
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());

        if(!userController.isLearning(user)){
            return ResponseMessage.builder()
                    .message(NEED_CHOOSE_GROUP_MESSAGE)
                    .chatId(user.getChatId())
                    .keyboard(replyKeyboardMarkupByState)
                    .build();
        }

        Verb learningVerb = learningController.getVerbForLearning(user);
        if(user.getState() != StateUser.LEARNING_IN_PROCESS_STATE) user = userController.setState(user, StateUser.LEARNING_IN_PROCESS_STATE);
        replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());

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
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());
        List<String> answerUserList = Arrays.stream(messageText.trim().replaceAll("[\\s]{2,}", " ").split(" ")).collect(Collectors.toList());
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
        user = userController.setState(user, StateUser.START_LEARN_STATE);
        Learning learningActive = learningController.getLearningActive(user);
        if(learningActive != null) learningController.setInactive(learningActive);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());
        return ResponseMessage.builder()
                .message(GOOD_WORK_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage startCreateGroup(User user) {
        user = userController.setState(user, StateUser.CREATE_GROUP_STATE);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());
        return ResponseMessage.builder()
                .message(INSTRUCTION_CREATE_GROUP_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage createGroup(User user, String messageText) {
        Set<String> verbsInfinitive = Arrays.stream(messageText.toLowerCase().split(" ")).collect(Collectors.toSet());
        HashMap<String, Verb> verbsInfinitiveHashMap = new HashMap<>();
        for (String verbInfinitive : verbsInfinitive) {
            verbsInfinitiveHashMap.put(verbInfinitive, verbController.getVerbByFirstForm(verbInfinitive));
        }

        Long idGroup = groupVerbController.createGroup(user).getId();
        List<Long> verbIds = verbsInfinitiveHashMap.values().stream()
                .filter(Objects::nonNull)
                .map(Verb::getId)
                .collect(Collectors.toList());

        String createGroupJson = gson.toJson(CreateGroupPojo.builder().idGroup(idGroup).verbIds(verbIds).build());
        user = userController.setTmp(user, createGroupJson);

        StringBuilder answer = new StringBuilder(RESULT_MESSAGE).append("\n");
        for (Map.Entry<String, Verb> one : verbsInfinitiveHashMap.entrySet()) {
            answer.append(one.getValue() == null ? ":cross_mark: " : ":check_mark: ").append(one.getKey()).append("\n");
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = keyboard.getInlineKeyboardMarkupByState(
                user.getState(),
                user.getChatId(),
                "-");
        return ResponseMessage.builder()
                .message(answer.toString())
                .chatId(user.getChatId())
                .keyboard(inlineKeyboardMarkup)
                .build();

    }

    public ResponseMessage saveGroup(User user){
        CreateGroupPojo createGroupPojo = gson.fromJson(user.getTmp(), CreateGroupPojo.class);
        compilationVerbController.saveVerbsInGroup(createGroupPojo.getIdGroup(), createGroupPojo.getVerbIds());
        user = userController.setState(user, StateUser.SET_NAME_GROUP_STATE);
        return ResponseMessage.builder()
                .message(SET_GROUP_NAME_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(ReplyKeyboardRemove.builder().removeKeyboard(true).build())
                .build();
    }

    public ResponseMessage setNameGroup(User user, String messageText) {
        user = userController.setState(user, userController.isLearning(user) ? StateUser.START_LEARN_STATE : StateUser.REGISTERED_STATE);
        CreateGroupPojo createGroupPojo = gson.fromJson(user.getTmp(), CreateGroupPojo.class);
        Compilation compilation = groupVerbController.getGroup(createGroupPojo.getIdGroup());
        groupVerbController.setName(compilation, messageText);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());
        String answer = String.format(GROUP_DONE_MESSAGE, messageText);
        return ResponseMessage.builder()
                .message(answer)
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage cancelSaveGroup(User user) {
        user = userController.setState(user, userController.isLearning(user) ? StateUser.START_LEARN_STATE : StateUser.REGISTERED_STATE);
        CreateGroupPojo createGroupPojo = gson.fromJson(user.getTmp(), CreateGroupPojo.class);
        groupVerbController.delete(createGroupPojo.getIdGroup());
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());
        return ResponseMessage.builder()
                .message(DO_NOT_SAVE_GROUP_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage startDeleteGroup(User user) {
        String answer;
        ReplyKeyboard keyboard1;
        if(user.getCompilations().size() > 0){
            user = userController.setState(user, DELETE_GROUP_STATE);
            answer = CHOOSE_GROUP_FOR_DELETE_MESSAGE;
            keyboard1 = keyboard.getInlineButtonsGroups(user.getCompilations());
        }else {
            answer = NO_GROUP_DELETE_MESSAGE;
            keyboard1 =  keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());
        }

        return ResponseMessage.builder()
                .message(answer)
                .chatId(user.getChatId())
                .keyboard(keyboard1)
                .build();
    }

    public ResponseMessage createConfirmDeleteGroup(User user, String data) {
        InlineKeyboardMarkup inlineButtonsGroups = keyboard.getConfirmDeleteButton(data);
        Compilation group = groupVerbController.getGroup(Long.parseLong(data));
        String answer = String.format(ARE_YOU_SURE_DELETE_GROUP_MESSAGE, group.getName());
        return ResponseMessage.builder()
                .message(answer)
                .chatId(user.getChatId())
                .keyboard(inlineButtonsGroups)
                .build();
    }

    public ResponseMessage deleteGroup(User user, String data) {
        user = userController.setState(user, userController.isLearning(user) ? StateUser.START_LEARN_STATE : StateUser.REGISTERED_STATE);
        long idGroup = Long.parseLong(data);
        Compilation group = groupVerbController.getGroup(idGroup);
        compilationVerbController.delete(group.getVerbs());
        groupVerbController.delete(idGroup);
        ReplyKeyboardMarkup replyKeyboardMarkupByState = keyboard.getReplyKeyboardMarkupByState(user.getState(), user.getChatId());
        return ResponseMessage.builder()
                .message(DELETE_GROUP_DONE_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboardMarkupByState)
                .build();
    }

    public ResponseMessage deleteKeyboard(User user){
        return ResponseMessage.builder()
                .message("-")
                .chatId(user.getChatId())
                .keyboard(keyboard.getRemoveKeyboard())
                .build();
    }

    public ResponseMessage allDeleteUser(User user) {
        Long chatId = user.getChatId();
        userController.delete(user);
        return ResponseMessage.builder()
                .message(DELETE_ALL_DATA_MESSAGE)
                .chatId(chatId)
                .keyboard(keyboard.getRemoveKeyboard())
                .build();
    }

}
