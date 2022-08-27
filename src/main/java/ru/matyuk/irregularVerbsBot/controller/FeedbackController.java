package ru.matyuk.irregularVerbsBot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Feedback;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.repository.FeedbackRepository;
import ru.matyuk.irregularVerbsBot.repository.UserRepository;

import java.util.List;

@Component
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    public void markToDelete(List<Feedback> feedbackList){
        User admin = userRepository.findById(1L).get();
        feedbackList.forEach(feedback -> {
            feedback.setUser(admin);
            feedbackRepository.save(feedback);
        });
    }


    public void create(User user, String messageText) {
        Feedback feedback = new Feedback();
        feedback.setText(messageText);
        feedback.setUser(user);
        feedbackRepository.save(feedback);
    }
}
