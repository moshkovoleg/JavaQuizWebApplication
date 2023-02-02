package com.moshkovoleg.javaquiz.repository;

import com.moshkovoleg.javaquiz.model.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long> {
}
