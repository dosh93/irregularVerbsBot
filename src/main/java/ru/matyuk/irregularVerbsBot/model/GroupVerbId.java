package ru.matyuk.irregularVerbsBot.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class GroupVerbId implements Serializable {

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "verb_id")
    private Long verbId;

    public GroupVerbId(){}
}
