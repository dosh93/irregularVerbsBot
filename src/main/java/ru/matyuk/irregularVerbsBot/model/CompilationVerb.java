package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CompilationVerb {

    @EmbeddedId
    private CompilationVerbId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("compilationId")
    @JoinColumn(name = "compilation_id")
    Compilation compilation;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("verbId")
    @JoinColumn(name = "verb_id")
    Verb verb;
}
