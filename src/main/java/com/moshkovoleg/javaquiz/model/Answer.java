package com.moshkovoleg.javaquiz.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Data
@EqualsAndHashCode(exclude = "question")
@Entity
public class Answer implements Serializable {
    @Id
//    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

//    private Long questionId;



    @Column(columnDefinition="TEXT", length = 1000)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public Answer() {
    }

    public Answer(Long id, String answer) {
        this.id = id;
        this.answer = answer;
    }


}
