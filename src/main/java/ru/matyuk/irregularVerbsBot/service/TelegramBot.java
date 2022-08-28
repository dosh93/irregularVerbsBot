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
import ru.matyuk.irregularVerbsBot.processing.*;
import ru.matyuk.irregularVerbsBot.config.BotConfig;
import ru.matyuk.irregularVerbsBot.config.InitMainCommands;
import ru.matyuk.irregularVerbsBot.controller.UserController;
import ru.matyuk.irregularVerbsBot.model.User;
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
            processingCallBackQuery(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            processingMessage(update.getMessage());
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
            allDeleteProcessing.processing(messageText, user);
            return;
        }

        switch (user.getState()){
            case CREATE_GROUP_STATE:
                createGroupProcessing.processing(messageText, user);
                break;
            case SET_NAME_GROUP_STATE:
                setNameGroupProcessing.processing(messageText, user);
                break;
            case LEARNING_STATE:
                learningProcessing.processing(messageText, user);
                break;
            case CREATE_FEEDBACK_STATE:
                deleteMessage(message.getMessageId(), user.getChatId());
                createFeedbackProcessing.processing(messageText, user);
                break;
            case MAIN_MENU_STATE:
                startProcessing.processing(messageText, user);
                break;
        }

    }

    private void processingCallBackQuery(CallbackQuery callbackQuery) {
        User user = userController.getUser(callbackQuery.getMessage().getChatId());
        switch (user.getState()){
            case MAIN_MENU_STATE:
                mainMenuProcessing.processing(callbackQuery, user);
                break;
            case VIEW_GROUP_STATE:
                viewGroupProcessing.processing(callbackQuery, user);
                break;
            case CHOOSE_GROUP_STATE:
                chooseGroupProcessing.processing(callbackQuery, user);
                break;
            case SETTING_GROUP_STATE:
                settingGroupProcessing.processing(callbackQuery, user);
                break;
            case CREATE_GROUP_STATE:
                createGroupProcessing.processing(callbackQuery, user);
                break;
            case CONFIRM_VERBS_IN_GROUP_STATE:
                confirmCreateGroupProcessing.processing(callbackQuery, user);
                break;
            case DELETE_GROUP_STATE:
                deleteGroupProcessing.processing(callbackQuery, user);
                break;
            case LEARNING_STATE:
                learningProcessing.processing(callbackQuery, user);
                break;
            case CREATE_FEEDBACK_STATE:
                createFeedbackProcessing.processing(callbackQuery, user);
                break;
            case ALL_DELETE_STATE:
                allDeleteProcessing.processing(callbackQuery, user);
                break;
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
}
