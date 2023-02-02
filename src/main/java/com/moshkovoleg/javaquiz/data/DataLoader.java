package com.moshkovoleg.javaquiz.data;

import com.moshkovoleg.javaquiz.model.Answer;

import com.moshkovoleg.javaquiz.model.Question;
import com.moshkovoleg.javaquiz.repository.AnswerRepository;
import com.moshkovoleg.javaquiz.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.*;
@Slf4j
@Controller
public class DataLoader implements CommandLineRunner {

    private QuestionRepository questionRepository;

    private AnswerRepository answerRepository;

    public DataLoader(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        long count = questionRepository.count();
        if (count == 0) {
            log.debug("Данные загружаются в базу...");
            dataLoad();
            log.debug("Данные загружены");
        }
    }
    @Transactional
    private void dataLoad() {
        Datasource.getInstance().open();
        Set<Question> questionList = Datasource.getInstance().queryQuestions(Datasource.ORDER_BY_NONE);
        Set<Answer> answerListWithQuestions = new HashSet<>();
        for (Question question : questionList) {
            Set<Answer> answerList = Datasource.getInstance().queryAnswersForQuestionId(question.getId().intValue());

            for (Answer answer : answerList) {
                answer.setQuestion(question);
            }
            question.setAnswers(answerList);
            answerListWithQuestions.addAll(answerList);


        }
        questionRepository.saveAll(questionList);
        answerRepository.saveAll(answerListWithQuestions);
        Datasource.getInstance().close();
    }
}

