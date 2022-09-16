package ru.matyuk.irregularVerbsBot.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.repository.VerbRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class VerbController {

    private final VerbRepository verbRepository;

    public VerbController(VerbRepository verbRepository) {
        this.verbRepository = verbRepository;
    }

    public Verb getVerbByFirstForm(String firstForm){
        return verbRepository.findByFirstForm(firstForm);
    }

    public List<Verb> getVerbsByIds(List<Long> verbsId) {
        return verbRepository.findByIdIn(verbsId);
    }

    public File getAudioFile(Verb verb, User user){
        if(verb.getAudio() != null && user.isViewAudio()){
            try {
                ClassPathResource classPathResource = new ClassPathResource(verb.getAudio());

                InputStream inputStream = classPathResource.getInputStream();
                File tmpAudioFile = File.createTempFile(UUID.randomUUID().toString(), ".aac");
                try {
                    FileUtils.copyInputStreamToFile(inputStream, tmpAudioFile);
                } finally {
                    IOUtils.close(inputStream);
                }
                return tmpAudioFile;
            } catch (IOException e) {
                log.error("Файл не найден " + e.getMessage());
            }
        }
        return null;
    }

    public long getCount(){
        return verbRepository.count();
    }

    public Verb getVerbById(long newId) {
        return verbRepository.findById(newId).orElse(null);
    }
}
