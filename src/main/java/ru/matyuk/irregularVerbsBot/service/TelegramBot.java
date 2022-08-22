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
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.matyuk.irregularVerbsBot.commandController.ResponseMessage;
import ru.matyuk.irregularVerbsBot.commandController.StartCommandController;
import ru.matyuk.irregularVerbsBot.config.BotConfig;
import ru.matyuk.irregularVerbsBot.config.Commands;
import ru.matyuk.irregularVerbsBot.controller.UserController;
import ru.matyuk.irregularVerbsBot.controller.VerbController;
import ru.matyuk.irregularVerbsBot.enums.Command;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.jsonPojo.CallbackQueryPojo;
import ru.matyuk.irregularVerbsBot.model.User;

import static ru.matyuk.irregularVerbsBot.config.Messages.RIGHT_MESSAGE;
import static ru.matyuk.irregularVerbsBot.enums.Command.BACK;
import static ru.matyuk.irregularVerbsBot.enums.Command.START;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserController userController;
    @Autowired
    private VerbController verbController;

    @Autowired
    private StartCommandController startCommandController;

    final BotConfig config;

    public TelegramBot(BotConfig config)
    {
        this.config = config;
        try {
            this.execute(new SetMyCommands(Commands.getCommands(), new BotCommandScopeDefault(), null));
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


    private void processingCallBackQuery(CallbackQuery callbackQuery){

        long chatId = callbackQuery.getMessage().getChatId();
        User user = userController.getUser(chatId);
        System.out.println(callbackQuery.getData());
        CallbackQueryPojo callbackQueryPojo = CallbackQueryPojo.getCallbackQueryPojo(callbackQuery.getData());

        if(callbackQueryPojo ==  null){
            sendMessage(startCommandController.unknownCommand(user));
            return;
        }

        Command command = callbackQueryPojo.getCommand();


        switch (user.getState()){
            case CREATE_GROUP_STATE:
                switch (command){
                    case SAVE:
                        deleteMessage(userController.getMessageIdCreateGroup(user), chatId);
                        sendMessage(startCommandController.saveGroup(user));
                        break;
                    case CANCEL:
                        deleteMessage(userController.getMessageIdCreateGroup(user), chatId);
                        sendMessage(startCommandController.cancelSaveGroup(user));
                        break;
                }
                break;
        }
    }

    private void deleteMessage(Integer messageId, long chatId) {
        try {
            execute(DeleteMessage.builder().messageId(messageId).chatId(String.valueOf(chatId)).build());
        } catch (TelegramApiException e) {
            log.error("Ошибка: " + e.getMessage());
        }
    }

    public void processingUserNull(Chat chat, Command command){
        if (command == START) {
            sendMessage(startCommandController.startCommand(chat));
        } else {
            sendMessage(startCommandController.unknownCommandNotUser(chat.getId()));
        }
    }

    private void processingMessage(Message message) {

        long chatId = message.getChatId();
        User user = userController.getUser(chatId);
        String messageText = message.getText();
        Command command = Command.fromString(messageText);

        if(user == null){
            processingUserNull(message.getChat(), command);
            return;
        }

        StateUser stateUser = user.getState();

        switch (stateUser){
            case REGISTERED_STATE:
            case START_LEARN_STATE:
                switch (command){
                    case VIEW_GROUP:
                        sendMessage(startCommandController.viewGroupVerb(user));
                        break;
                    case CHOOSE_GROUP:
                        sendMessage(startCommandController.chooseGroupVerb(user));
                        break;
                    case LEARNING:
                        sendMessage(startCommandController.learning(user));
                        break;
                    case CREATE_GROUP:
                        sendMessage(startCommandController.startCreateGroup(user));
                        break;
                    default: sendMessage(startCommandController.unknownCommand(user));
                }
                break;
            case VIEW_GROUP_STATE:
                if(command.equals(BACK)){
                    sendMessage(startCommandController.goToMainMenu(user));
                    break;
                }
                sendMessage(startCommandController.viewSelectedGroupVerb(user, messageText));
                break;
            case CHOOSE_GROUP_STATE:
                if(command.equals(BACK)){
                    sendMessage(startCommandController.goToMainMenu(user));
                    break;
                }
                sendMessage(startCommandController.startLearning(user, messageText));
                break;
            case LEARNING_IN_PROCESS_STATE:
                switch (command){
                    case END:
                        sendMessage(startCommandController.stopLearning(user));
                        break;
                    default:
                        ResponseMessage responseMessage = startCommandController.checkAnswer(user, messageText);
                        sendMessage(responseMessage);
                        if(responseMessage.getMessage().equals(RIGHT_MESSAGE)){
                            user = userController.getUser(chatId);
                            sendMessage(startCommandController.learning(user));
                        }
                }
                break;
            case CREATE_GROUP_STATE:
                startCommandController.saveMessageIdCreateGroup(sendMessage(startCommandController.createGroup(user, messageText)), user);
                break;
            case SET_NAME_GROUP_STATE:
                sendMessage(startCommandController.setNameGroup(user, messageText));
                break;
            default:
                sendMessage(startCommandController.unknownCommand(user));
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasCallbackQuery()){
            processingCallBackQuery(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            processingMessage(update.getMessage());
        }
    }


    private Integer sendMessage(ResponseMessage responseMessage){
        SendMessage message = new SendMessage();
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
}
