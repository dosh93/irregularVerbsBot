package ru.matyuk.irregularVerbsBot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Compilation;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.repository.GroupVerbRepository;

import java.util.*;

@Slf4j
@Component
public class GroupVerbController {

    @Autowired
    private GroupVerbRepository groupVerbRepository;


    public List<Compilation> getGroupsWithVerbsByChatId(Long chatId){
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(chatId);
        List<Compilation> all = groupVerbRepository.findByUserChatIdIn(ids);
        List<Compilation> result = new ArrayList<>();
        for (Compilation groupVerb : all) {
            if(!groupVerb.getVerbs().isEmpty()) result.add(groupVerb);
        }
        return result;
    }

    public Compilation getGroup(String name){
        return groupVerbRepository.findByName(name);
    }

    public Compilation createGroup(User user){
        Compilation compilation = new Compilation();
        compilation.setUser(user);
        compilation.setName(user.getChatId().toString());
        return groupVerbRepository.save(compilation);
    }

    public Compilation getGroup(Long compilationId) {
        return groupVerbRepository.findById(compilationId).get();
    }

    public Compilation setName(Compilation compilation, String messageText) {
        compilation.setName(messageText);
        return groupVerbRepository.save(compilation);
    }
}
