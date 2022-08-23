package ru.matyuk.irregularVerbsBot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.repository.UserRepository;

@Component
public class OnApplicationStartUp {

    @Autowired
    private UserRepository userRepository;


    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepository.findById(1L).isEmpty()) {
            User user = new User();
            user.setChatId(1L);
            user.setFirstName("Admin");
            userRepository.save(user);
        }
    }
}
