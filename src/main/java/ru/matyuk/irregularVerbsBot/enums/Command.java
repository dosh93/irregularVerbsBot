package ru.matyuk.irregularVerbsBot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

@Getter
public enum Command {
    BACK("Назад"),
    END("Закончить"),
    START("/start"),
    LEARNING("Учится"),
    CHOOSE_GROUP("Выбор группы глаголов"),
    VIEW_GROUP("Просмотр групп глаголов"),
    UNKNOWN_COMMAND("Не известная команда");

    private final String name;

    private final static Map<String, Command> map =
            stream(Command.values()).collect(toMap(command -> command.name, command -> command));

    Command(final String name) {
        this.name = name;
    }

    public static Command fromString(String names) {
        Command command = map.get(names);
        if(command == null) command = UNKNOWN_COMMAND;
        return command;
    }

}
