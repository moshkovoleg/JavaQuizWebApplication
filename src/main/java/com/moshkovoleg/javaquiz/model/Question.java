package com.moshkovoleg.javaquiz.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.*;


@Data
@EqualsAndHashCode(exclude = "answer")
@Entity
public class Question implements Serializable {
    @Id
//    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition="TEXT", length = 1000)
    private String text;
    @Column(columnDefinition="TEXT", length = 1000)
    private String correctAnswer;
    @Column(columnDefinition="TEXT", length = 1000)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    private Set<Answer> answers = new HashSet<>();



    public Question() {
    }

    public Question(Long id, String text, String correctAnswer, String description) {
        this.id = id;
        this.text = text;
        this.correctAnswer = correctAnswer;
        this.description = description;

    }


}
