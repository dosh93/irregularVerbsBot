package ru.matyuk.irregularVerbsBot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Learning;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.UserVerbId;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.repository.LearningRepository;
import ru.matyuk.irregularVerbsBot.utils.CommonUtils;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class LearningController {

    @Autowired
    private LearningRepository learningRepository;

    @Autowired
    private VerbController verbController;

    private int MAX_COUNT_SUCCESSFUL = 3;

    public void addToLearning(User user, List<Verb> verbs){
        Set<Long> idsVerb = learningRepository.findByUser(user)
                .stream()
                .map(learning -> learning.getVerb().getId())
                .collect(Collectors.toSet());
        for (Verb verb : verbs) {
            if(!idsVerb.contains(verb.getId())){
                Learning learning = new Learning();
                learning.setUser(user);
                learning.setVerb(verb);
                learning.setId(new UserVerbId(user.getChatId(), verb.getId()));
                learningRepository.save(learning);
            }
        }
    }

    public Verb getVerbForLearning(User user) {
        List<Learning> learnings = learningRepository.findByUserAndCountSuccessfulLessThanOrderByCountSuccessfulAsc(user, MAX_COUNT_SUCCESSFUL);
        if(learnings.size() == 0)
            return null;
        int minCountSuccessful = learnings.get(0).getCountSuccessful();
        List<Learning> learningForLearn = new ArrayList<>();
        for (Learning learning : learnings) {
            if(learning.getCountSuccessful() == minCountSuccessful) learningForLearn.add(learning);
            else break;
        }
        int randomNumber = CommonUtils.getRandomNumber(0, learningForLearn.size());
        Learning learning = learningForLearn.get(randomNumber);
        learning.setState(true);
        learningRepository.save(learning);
        return learning.getVerb();
    }

    public Learning getLearningActive(User user) {
        return learningRepository.findByUserAndState(user, true);
    }

    public void setInactiveAndAddSuccessful(Learning learningVerb) {
        System.out.println("---------------------------------------------------------------------------------------");
        learningVerb.setState(false);
        learningVerb.setCountSuccessful(learningVerb.getCountSuccessful() + 1);
        learningRepository.save(learningVerb);
    }

    public void setInactive(Learning learningVerb) {
        System.out.println("---------------------------------------------------------------------------------------");
        learningVerb.setState(false);
        learningRepository.save(learningVerb);
    }

    public boolean isValidAnswerUser(List<String> answerUserList, Learning learningVerb){
        return checkVerb(learningVerb.getVerb().getSecondForm(), answerUserList.get(0))
                && checkVerb(learningVerb.getVerb().getThirdForm(), answerUserList.get(1));
    }

    private boolean checkVerb(String correct, String answer){
        correct = correct.toLowerCase();
        answer = answer.toLowerCase();
        if(correct.contains("/")){
            Set<String> collect = Arrays.stream(correct.split("/")).collect(Collectors.toSet());
            if(answer.contains("/")){
                Set<String> answers = Arrays.stream(answer.split("/")).collect(Collectors.toSet());
                for (String one: answers) if(!collect.contains(one)) return false;
            }else return collect.contains(answer);

        }else return correct.equals(answer);

        return true;
    }

    public void resetCountSuccessful(Learning learningVerb) {
        learningVerb.setCountSuccessful(0);
        learningRepository.save(learningVerb);
    }
}
