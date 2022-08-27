package ru.matyuk.irregularVerbsBot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.matyuk.irregularVerbsBot.model.Feedback;

public interface FeedbackRepository extends CrudRepository<Feedback, Long> {
}
