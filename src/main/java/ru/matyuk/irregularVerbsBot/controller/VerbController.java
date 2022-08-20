package ru.matyuk.irregularVerbsBot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.repository.VerbRepository;

@Component
@Slf4j
public class VerbController {

    @Autowired
    private VerbRepository verbRepository;

}
