package com.moshkovoleg.javaquiz.controller;

import com.moshkovoleg.javaquiz.model.Question;
import com.moshkovoleg.javaquiz.service.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @GetMapping("/question/{id}")
    public String startQuiz(@PathVariable(name="id") long id, Model model) {

        model.addAttribute("title", "Java Викторина");


        model.addAttribute("question", quizService.getQuestionById(id));
        return "quiz";
    }

    @PostMapping("recipe")
    public String nextQuestion(@ModelAttribute Question question){

        return "redirect:/question/" + (question.getId()+1);
    }

}
