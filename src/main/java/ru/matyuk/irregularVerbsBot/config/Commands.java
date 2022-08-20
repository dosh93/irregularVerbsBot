package ru.matyuk.irregularVerbsBot.config;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    public static List<BotCommand> getCommands(){
        List<BotCommand> listCommand = new ArrayList<>();
        listCommand.add(new BotCommand("/start", "Начало работы"));
        listCommand.add(new BotCommand("/help", "Помощь"));
        return listCommand;
    }
}
