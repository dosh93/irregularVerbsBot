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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.matyuk.irregularVerbsBot.Keyboard;
import ru.matyuk.irregularVerbsBot.commandController.ResponseMessage;
import ru.matyuk.irregularVerbsBot.commandController.StartCommandController;
import ru.matyuk.irregularVerbsBot.config.BotConfig;
import ru.matyuk.irregularVerbsBot.config.Commands;
import ru.matyuk.irregularVerbsBot.controller.UserController;
import ru.matyuk.irregularVerbsBot.controller.VerbController;
import ru.matyuk.irregularVerbsBot.enums.Command;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.User;

import java.util.ArrayList;
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

        if(update.hasMessage() && update.getMessage().hasText()){
            Message message = update.getMessage();
            String messageText = message.getText();
            Command command = Command.fromString(messageText);
            long chatId = message.getChatId();
            User user = userController.getUser(chatId);

            log.info(String.format("Запрос от пользователя chatId = %d message = %s", chatId, messageText));

            if(user != null){
                switch (user.getState()){
                    case REGISTERED:
                    case START_LEARN:
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
                            default: sendMessage(startCommandController.unknownCommand(user));
                        }
                        break;
                    case VIEW_GROUP:
                        if(command.equals(BACK)){
                            sendMessage(startCommandController.goToMainMenu(user));
                            break;
                        }
                        sendMessage(startCommandController.viewSelectedGroupVerb(user, messageText));
                        break;
                    case CHOOSE_GROUP:
                        if(command.equals(BACK)){
                            sendMessage(startCommandController.goToMainMenu(user));
                            break;
                        }
                        sendMessage(startCommandController.startLearning(user, messageText));
                        break;
                    case LEARNING_IN_PROCESS:
                        switch (command){
                            case END:
                                sendMessage(startCommandController.stopLearning(user));
                                break;
                            default:
                                ResponseMessage responseMessage = startCommandController.checkAnswer(user, messageText);
                                sendMessage(responseMessage);
                                if(responseMessage.getMessage().equals(RIGHT_MESSAGE))
                                    user = userController.getUser(chatId);
                                    sendMessage(startCommandController.learning(user));
                        }
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
