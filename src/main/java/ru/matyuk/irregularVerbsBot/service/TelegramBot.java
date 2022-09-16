package ru.matyuk.irregularVerbsBot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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
import static ru.matyuk.irregularVerbsBot.utils.CommonUtils.getDeleteAudio;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final UserController userController;
    private final StartProcessing startProcessing;
    private final MainMenuProcessing mainMenuProcessing;
    private final ViewGroupProcessing viewGroupProcessing;
    private final ChooseGroupProcessing chooseGroupProcessing;
    private final SettingGroupProcessing settingGroupProcessing;
    private final CreateGroupProcessing createGroupProcessing;
    private final ConfirmCreateGroupProcessing confirmCreateGroupProcessing;
    private final DeleteGroupProcessing deleteGroupProcessing;
    private final LearningProcessing learningProcessing;
    private final AllDeleteProcessing allDeleteProcessing;
    private final CreateFeedbackProcessing createFeedbackProcessing;
    private final SetNameGroupProcessing setNameGroupProcessing;
    private final SettingLearningProcessing settingLearningProcessing;
    private final ChooseGroupResetProcessing chooseGroupResetProcessing;
    private final SettingMainProcessing settingMainProcessing;
    private final SetCountSuccessfulProcessing setCountSuccessfulProcessing;
    private final ChallengeProcessing challengeProcessing;

    final BotConfig config;

    public TelegramBot(BotConfig config, UserController userController, StartProcessing startProcessing, SetNameGroupProcessing setNameGroupProcessing, MainMenuProcessing mainMenuProcessing, ViewGroupProcessing viewGroupProcessing, ChooseGroupResetProcessing chooseGroupResetProcessing, ChooseGroupProcessing chooseGroupProcessing, SettingGroupProcessing settingGroupProcessing, CreateGroupProcessing createGroupProcessing, ConfirmCreateGroupProcessing confirmCreateGroupProcessing, SettingLearningProcessing settingLearningProcessing, SettingMainProcessing settingMainProcessing, DeleteGroupProcessing deleteGroupProcessing, LearningProcessing learningProcessing, AllDeleteProcessing allDeleteProcessing, CreateFeedbackProcessing createFeedbackProcessing, SetCountSuccessfulProcessing setCountSuccessfulProcessing, ChallengeProcessing challengeProcessing)
    {
        this.config = config;
        this.setCountSuccessfulProcessing = setCountSuccessfulProcessing;
        try {
            this.execute(new SetMyCommands(InitMainCommands.getCommands(), new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){
            log.error("Ошибка создания меню " + e.getMessage());
        }
        this.userController = userController;
        this.startProcessing = startProcessing;
        this.setNameGroupProcessing = setNameGroupProcessing;
        this.mainMenuProcessing = mainMenuProcessing;
        this.viewGroupProcessing = viewGroupProcessing;
        this.chooseGroupResetProcessing = chooseGroupResetProcessing;
        this.chooseGroupProcessing = chooseGroupProcessing;
        this.settingGroupProcessing = settingGroupProcessing;
        this.createGroupProcessing = createGroupProcessing;
        this.confirmCreateGroupProcessing = confirmCreateGroupProcessing;
        this.settingLearningProcessing = settingLearningProcessing;
        this.settingMainProcessing = settingMainProcessing;
        this.deleteGroupProcessing = deleteGroupProcessing;
        this.learningProcessing = learningProcessing;
        this.allDeleteProcessing = allDeleteProcessing;
        this.createFeedbackProcessing = createFeedbackProcessing;
        this.challengeProcessing = challengeProcessing;
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
            case SET_COUNT_SUCCESSFUL_STATE:
                processingResponse(setCountSuccessfulProcessing.processing(messageText, user));
                break;
            case CHALLENGE_STATE:
                processingResponse(challengeProcessing.processing(messageText, user));
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
            case SET_COUNT_SUCCESSFUL_STATE:
                processingResponse(setCountSuccessfulProcessing.processing(callbackQuery, user));
                break;
            case CHALLENGE_STATE:
                processingResponse(challengeProcessing.processing(callbackQuery, user));
                break;
        }
    }


    private void processingResponse(Response response){
        if(response == null) return;

        if(response.getDeleteMessage() != null){
            response.getDeleteMessage().forEach(this::deleteMessage);
        }

        if (response.getResponseMessage() != null){
            if(response.getResponseMessage().getAudio() != null){
                DeleteMessage deleteAudio = getDeleteAudio(response.getUser());
                if(deleteAudio != null) deleteMessage(deleteAudio);

                int messageId = sendAudio(response.getResponseMessage());
                userController.setTmp(response.getUser(), "audioId:" + messageId);
            }

            int messageId = sendMessage(response.getResponseMessage());
            if(response.isSaveSentMessageId()){
                userController.setTmp(response.getUser(), String.valueOf(messageId));
            }
        }
    }

    public Integer sendAudio(ResponseMessage responseMessage){
        SendAudio sendAudio = new SendAudio();
        InputFile inputFile = new InputFile();
        inputFile.setMedia(responseMessage.getAudio(), responseMessage.getAudioName());
        sendAudio.setAudio(inputFile);
        sendAudio.setChatId(String.valueOf(responseMessage.getChatId()));
        try {
            return execute(sendAudio).getMessageId();
        } catch (TelegramApiException e) {
            log.error("Ошибка: " + e.getMessage());
        }
        return -1;
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
