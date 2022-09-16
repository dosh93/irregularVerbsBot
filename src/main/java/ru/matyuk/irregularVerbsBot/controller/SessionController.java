package ru.matyuk.irregularVerbsBot.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.jsonPojo.ChallengeVerbPojo;
import ru.matyuk.irregularVerbsBot.model.Session;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.repository.SessionRepository;

import java.sql.Timestamp;

@Component
public class SessionController {

    private final SessionRepository sessionRepository;
    private final VerbController verbController;

    public SessionController(SessionRepository sessionRepository, VerbController verbController) {
        this.sessionRepository = sessionRepository;
        this.verbController = verbController;
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

    public Session createSessionChallenge(User user, int count) {
        Session session = new Session();
        session.setState(true);
        session.setFail(0);
        session.setCount(count);
        session.setSuccess(0);
        session.setUser(user);
        session.setStart(new Timestamp(System.currentTimeMillis()));
        session.setJson("{\"verbIds\":[]}");
        return sessionRepository.save(session);
    }

    public Verb getVerbActive(User user) {
        Gson gson = new Gson();
        Session activeSession = user.getActiveSession();
        ChallengeVerbPojo challengeVerbPojo = gson.fromJson(activeSession.getJson(), ChallengeVerbPojo.class);
        Long last = challengeVerbPojo.getVerbIds().getLast();
        return verbController.getVerbById(last);
    }
}
