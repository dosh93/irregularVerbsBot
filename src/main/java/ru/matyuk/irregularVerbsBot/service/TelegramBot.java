package ru.matyuk.irregularVerbsBot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.processing.*;
import ru.matyuk.irregularVerbsBot.config.BotConfig;
import ru.matyuk.irregularVerbsBot.config.InitMainCommands;
import ru.matyuk.irregularVerbsBot.controller.UserController;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.Response;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;

import static ru.matyuk.irregularVerbsBot.enums.MainCommands.ALL_DELETE;
import static ru.matyuk.irregularVerbsBot.enums.MainCommands.START;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserController userController;

    @Autowired
    private StartProcessing startProcessing;

    @Autowired
    private MainMenuProcessing mainMenuProcessing;

    @Autowired
    private ViewGroupProcessing viewGroupProcessing;

    @Autowired
    private ChooseGroupProcessing chooseGroupProcessing;

    @Autowired
    private SettingGroupProcessing settingGroupProcessing;

    @Autowired
    private CreateGroupProcessing createGroupProcessing;

    @Autowired
    private ConfirmCreateGroupProcessing confirmCreateGroupProcessing;

    @Autowired
    private DeleteGroupProcessing deleteGroupProcessing;

    @Autowired
    private LearningProcessing learningProcessing;

    @Autowired
    private AllDeleteProcessing allDeleteProcessing;

    @Autowired
    private CreateFeedbackProcessing createFeedbackProcessing;

    @Autowired
    private SetNameGroupProcessing setNameGroupProcessing;

    @Autowired
    private SettingLearningProcessing settingLearningProcessing;

    @Autowired
    private ChooseGroupResetProcessing chooseGroupResetProcessing;

    @Autowired
    private SettingMainProcessing settingMainProcessing;


    final BotConfig config;

    public TelegramBot(BotConfig config)
    {
        this.config = config;
        try {
            this.execute(new SetMyCommands(InitMainCommands.getCommands(), new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){
            log.error("Ошибка создания меню " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasCallbackQuery()){
            Runnable processCallback = () -> {
                processingCallBackQuery(update.getCallbackQuery());
            };
            Thread thread = new Thread(processCallback);
            thread.start();
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            Runnable processMessage = () -> {
                processingMessage(update.getMessage());
            };
            Thread thread = new Thread(processMessage);
            thread.start();
        }
    }

    private void processingMessage(Message message) {
        User user = userController.getUser(message.getChatId());
        String messageText = message.getText().toLowerCase().trim().replaceAll("[\\s]{2,}", " ");

        if(message.getText().equals(START.getName()) && user == null){
            userController.registerUser(message.getChat());
            user = userController.getUser(message.getChatId());
        } else if (message.getText().equals(ALL_DELETE.getName())) {
            deleteMessage(message.getMessageId(), user.getChatId());
            processingResponse(allDeleteProcessing.processing(messageText, user));
            return;
        }

        switch (user.getState()){
            case CREATE_GROUP_STATE:
                processingResponse(createGroupProcessing.processing(messageText, user));
                break;
            case SET_NAME_GROUP_STATE:
                deleteMessage(message.getMessageId(), user.getChatId());
                processingResponse(setNameGroupProcessing.processing(messageText, user));
                break;
            case LEARNING_STATE:
                processingResponse(learningProcessing.processing(messageText, user));
                break;
            case CREATE_FEEDBACK_STATE:
                deleteMessage(message.getMessageId(), user.getChatId());
                processingResponse(createFeedbackProcessing.processing(messageText, user));
                break;
            case MAIN_MENU_STATE:
                deleteMessage(message.getMessageId(), user.getChatId());
                deleteMessage(message.getMessageId() - 1, user.getChatId());
                processingResponse(startProcessing.processing(messageText, user));
                break;
            default:
                deleteMessage(message.getMessageId(), user.getChatId());
        }

    }

    private void processingCallBackQuery(CallbackQuery callbackQuery) {
        if(callbackQuery.getData().equals(ButtonCommand.NONE.name())){
            return;
        }

        User user = userController.getUser(callbackQuery.getMessage().getChatId());
        switch (user.getState()){
            case MAIN_MENU_STATE:
                processingResponse(mainMenuProcessing.processing(callbackQuery, user));
                break;
            case VIEW_GROUP_STATE:
                processingResponse(viewGroupProcessing.processing(callbackQuery, user));
                break;
            case CHOOSE_GROUP_STATE:
                processingResponse(chooseGroupProcessing.processing(callbackQuery, user));
                break;
            case SETTING_GROUP_STATE:
                processingResponse(settingGroupProcessing.processing(callbackQuery, user));
                break;
            case CREATE_GROUP_STATE:
                processingResponse(createGroupProcessing.processing(callbackQuery, user));
                break;
            case CONFIRM_VERBS_IN_GROUP_STATE:
                processingResponse(confirmCreateGroupProcessing.processing(callbackQuery, user));
                break;
            case DELETE_GROUP_STATE:
                processingResponse(deleteGroupProcessing.processing(callbackQuery, user));
                break;
            case LEARNING_STATE:
                processingResponse(learningProcessing.processing(callbackQuery, user));
                break;
            case CREATE_FEEDBACK_STATE:
                processingResponse(createFeedbackProcessing.processing(callbackQuery, user));
                break;
            case ALL_DELETE_STATE:
                processingResponse(allDeleteProcessing.processing(callbackQuery, user));
                break;
            case SETTING_LEARNING_STATE:
                processingResponse(settingLearningProcessing.processing(callbackQuery, user));
                break;
            case CHOOSE_GROUP_RESET_STATE:
                processingResponse(chooseGroupResetProcessing.processing(callbackQuery, user));
                break;
            case SETTING_MAIN_STATE:
                processingResponse(settingMainProcessing.processing(callbackQuery, user));
        }
    }


    private void processingResponse(Response response){
        if(response == null) return;

        if(response.getDeleteMessage() != null){
            deleteMessage(response.getDeleteMessage());
        }

        if (response.getResponseMessage() != null){
            int messageId = sendMessage(response.getResponseMessage());
            if(response.isSaveSentMessageId()){
                userController.setTmp(response.getUser(), String.valueOf(messageId));
            }
        }
    }

    public Integer sendMessage(ResponseMessage responseMessage){
        SendMessage message = new SendMessage();
        message.enableHtml(true);
        message.setChatId(String.valueOf(responseMessage.getChatId()));
        message.setText(EmojiParser.parseToUnicode(responseMessage.getMessage()));

        if(responseMessage.getKeyboard() != null) message.setReplyMarkup(responseMessage.getKeyboard());
        try {
            return execute(message).getMessageId();
        }catch (TelegramApiException e){
            log.error("Ошибка: " + e.getMessage());
        }
        return -1;
    }

    public void deleteMessage(Integer messageId, long chatId) {
        try {
            execute(DeleteMessage.builder().messageId(messageId).chatId(String.valueOf(chatId)).build());
        } catch (TelegramApiException e) {
            log.error("Ошибка: " + e.getMessage());
        }
    }

    public void deleteMessage(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка: " + e.getMessage());
        }
    }
}
