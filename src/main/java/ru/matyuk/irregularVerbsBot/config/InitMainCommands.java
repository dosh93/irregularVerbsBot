package ru.matyuk.irregularVerbsBot.config;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.matyuk.irregularVerbsBot.enums.MainCommands;

import java.util.ArrayList;
import java.util.List;

public class InitMainCommands {

    public static List<BotCommand> getCommands(){
        List<BotCommand> listCommand = new ArrayList<>();
        listCommand.add(new BotCommand(MainCommands.START.getName(), "Начать учить"));
        listCommand.add(new BotCommand(MainCommands.ALL_DELETE.getName(), "Начать всё сначало"));
        return listCommand;
    }
}
