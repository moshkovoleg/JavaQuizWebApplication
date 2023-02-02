package com.moshkovoleg.javaquiz.repository;

import com.moshkovoleg.javaquiz.model.Answer;
import org.springframework.data.repository.CrudRepository;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
}
