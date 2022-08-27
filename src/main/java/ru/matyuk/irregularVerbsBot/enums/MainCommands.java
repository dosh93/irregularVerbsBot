package ru.matyuk.irregularVerbsBot.enums;

import lombok.Getter;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

@Getter
public enum MainCommands {

    START("/start"),
    ALL_DELETE("/all_delete");

    private final String name;

    private final static Map<String, MainCommands> map =
            stream(MainCommands.values()).collect(toMap(command -> command.name, command -> command));

    MainCommands(final String name) {
        this.name = name;
    }

}
