package ru.matyuk.irregularVerbsBot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.repository.UserRepository;

import java.sql.Timestamp;
import java.util.Optional;

@Component
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(Message message) {
        User user = new User();
        Optional<User> optionalUser = userRepository.findById(message.getChatId());
        if(optionalUser.isEmpty()) {
            var chatId = message.getChatId();
            var chat = message.getChat();

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisterAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("Сохранен юзер " + user);
        }else user = optionalUser.get();

        return user;
    }

    public User getUser(long chatId){
        if (userRepository.findById(chatId).isEmpty())
            return null;
        return userRepository.findById(chatId).get();
    }

    public User setState(long chatId, StateUser state){
        Optional<User> optionalUser = userRepository.findById(chatId);
        if (optionalUser.isEmpty())
            return null;
        User user = optionalUser.get();
        user.setState(state);
        userRepository.save(user);
        return user;
    }

    public boolean isLearning(User user){
        return user.getLearnings().size() != 0;
    }


}
