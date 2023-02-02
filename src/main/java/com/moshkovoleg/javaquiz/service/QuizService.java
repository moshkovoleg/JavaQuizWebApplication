package com.moshkovoleg.javaquiz.service;

import com.moshkovoleg.javaquiz.model.Answer;
import com.moshkovoleg.javaquiz.model.Question;

import java.util.Set;

public interface QuizService {

        Set<Answer> getAnswers();
        Set<Question> getQuestions();
        Question getQuestionById(Long id);


}
