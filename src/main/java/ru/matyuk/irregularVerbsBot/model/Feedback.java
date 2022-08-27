package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "tg_feedbacks")
@Data
public class Feedback {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Lob
    @Basic(fetch=LAZY)
    private String text;
}
