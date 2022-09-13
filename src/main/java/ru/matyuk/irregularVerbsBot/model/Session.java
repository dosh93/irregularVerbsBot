package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;

import javax.persistence.*;

import java.sql.Timestamp;

import static org.hibernate.id.PersistentIdentifierGenerator.IDENTIFIER_NORMALIZER;

@Entity(name = "tg_session")
@Data
public class Session {

    @Id
    @GeneratedValue(generator = IDENTIFIER_NORMALIZER)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private int count;

    private int success;

    private int fail;

    private Timestamp start;

    private Timestamp stop;

    @Column(columnDefinition = "bit(1) default 0")
    private boolean state;
}
