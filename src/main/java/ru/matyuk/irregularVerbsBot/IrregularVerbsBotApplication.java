package ru.matyuk.irregularVerbsBot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.repository.GroupVerbRepository;

@SpringBootApplication
public class IrregularVerbsBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrregularVerbsBotApplication.class, args);
	}

}
