package com.moshkovoleg.javaquiz.service;

import com.moshkovoleg.javaquiz.model.Answer;
import com.moshkovoleg.javaquiz.model.Question;
import com.moshkovoleg.javaquiz.repository.AnswerRepository;
import com.moshkovoleg.javaquiz.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Slf4j
@Service
public class QuizServiceImpl implements QuizService {

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    public QuizServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public Set<Answer> getAnswers() {
        log.debug("I'm in answer service");
        Set<Answer> listOfAnswers = new HashSet<>();
        answerRepository.findAll().iterator().forEachRemaining(listOfAnswers::add);
        return listOfAnswers;
    }

    @Override
    public Set<Question> getQuestions() {
        log.debug("I'm in question service");
        Set<Question> listOfQuestions = new HashSet<>();
        questionRepository.findAll().iterator().forEachRemaining(listOfQuestions::add);
        return listOfQuestions;
    }

    @Override
    public Question getQuestionById(Long id) {
        Optional<Question> optionalQuestion = questionRepository.findById(id);
        if (optionalQuestion.isEmpty()) throw new RuntimeException("Question not found");

        return optionalQuestion.get();
    }


}
