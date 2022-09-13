package ru.matyuk.irregularVerbsBot.controller;

import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Session;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.repository.SessionRepository;

import java.sql.Timestamp;

@Component
public class SessionController {

    private final SessionRepository sessionRepository;

    public SessionController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }


    public Session createSession(User user) {
        Session session = new Session();
        session.setState(true);
        session.setFail(0);
        session.setCount(-1);
        session.setSuccess(0);
        session.setUser(user);
        session.setStart(new Timestamp(System.currentTimeMillis()));
        return sessionRepository.save(session);
    }
}
