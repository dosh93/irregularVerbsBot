package ru.matyuk.irregularVerbsBot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Learning;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.id.UserVerbId;
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

    @Value("${learning.count_successful}")
    private int MAX_COUNT_SUCCESSFUL;


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
        learningVerb.setState(false);
        learningVerb.setCountSuccessful(learningVerb.getCountSuccessful() + 1);
        learningRepository.save(learningVerb);
    }

    public void setInactive(Learning learningVerb) {
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

    public void delete(List<Learning> learnings) {
        learnings.forEach(learning -> learningRepository.delete(learning));
    }

    public Learning getLearning(UserVerbId userVerbId) {
        Optional<Learning> byId = learningRepository.findById(userVerbId);
        return byId.orElse(null);
    }
}
