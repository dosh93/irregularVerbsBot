package ru.matyuk.irregularVerbsBot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.matyuk.irregularVerbsBot.commandController.ResponseMessage;
import ru.matyuk.irregularVerbsBot.commandController.StartCommandController;
import ru.matyuk.irregularVerbsBot.config.BotConfig;
import ru.matyuk.irregularVerbsBot.config.Commands;
import ru.matyuk.irregularVerbsBot.controller.UserController;
import ru.matyuk.irregularVerbsBot.controller.VerbController;
import ru.matyuk.irregularVerbsBot.enums.Command;
import ru.matyuk.irregularVerbsBot.model.User;

import java.util.Arrays;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.config.Messages.RIGHT_MESSAGE;
import static ru.matyuk.irregularVerbsBot.enums.Command.BACK;

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

    @Override
    public void onUpdateReceived(Update update) {

        Message message = null;
        String messageText = "";
        Command command;
        long chatId;
        if(update.hasCallbackQuery()){
            List<String> dataList = Arrays.asList(update.getCallbackQuery().getData().split(":"));
            command = Command.fromString(dataList.get(0));
            chatId = Long.parseLong(dataList.get(1));
        }else {
            message = update.getMessage();
            messageText = message.getText();
            command = Command.fromString(messageText);
            chatId = message.getChatId();
        }
        User user = userController.getUser(chatId);

        log.info(String.format("Запрос от пользователя chatId = %d message = %s", chatId, messageText));

        if(user != null){
            switch (user.getState()){
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
                case CREATE_GROUP_STATE:
                    if(update.hasCallbackQuery()){
                        switch (command){
                            case SAVE:
                                System.out.println("SSAVE");
                                break;
                            case CANCEL:
                                System.out.println("CANCEL");
                                break;
                        }
                    }else {
                        sendMessage(startCommandController.createGroup(user, messageText));
                    }
                    break;

            }
        }else {
            switch (command){
                case START:
                    sendMessage(startCommandController.startCommand(message));
                    break;
                default: sendMessage(startCommandController.unknownCommand(user));
            }
        }

    }

    private void sendMessage(ResponseMessage responseMessage){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(responseMessage.getChatId()));
        message.setText(EmojiParser.parseToUnicode(responseMessage.getMessage()));

        if(responseMessage.getKeyboard() != null) message.setReplyMarkup(responseMessage.getKeyboard());
        try {
            execute(message);
        }catch (TelegramApiException e){
            log.error("Ошибка: " + e.getMessage());
        }
    }
}
