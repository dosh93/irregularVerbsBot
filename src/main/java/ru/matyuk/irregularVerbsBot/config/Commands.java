package ru.matyuk.irregularVerbsBot.config;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.matyuk.irregularVerbsBot.enums.Command;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    public static List<BotCommand> getCommands(){
        List<BotCommand> listCommand = new ArrayList<>();
        listCommand.add(new BotCommand(Command.START.getName(), "Начать учить"));
        listCommand.add(new BotCommand(Command.HELP.getName(), "Помощь"));
        listCommand.add(new BotCommand(Command.ALL_DELETE.getName(), "Удалить все данные"));
        listCommand.add(new BotCommand(Command.FEEDBACK.getName(), "Обратная связь"));
        return listCommand;
    }
}
