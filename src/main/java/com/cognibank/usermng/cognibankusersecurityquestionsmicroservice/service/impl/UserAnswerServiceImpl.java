package com.cognibank.usermng.cognibankusersecurityquestionsmicroservice.service.impl;

import com.cognibank.usermng.cognibankusersecurityquestionsmicroservice.model.SecurityQuestion;
import com.cognibank.usermng.cognibankusersecurityquestionsmicroservice.model.UserAnswers;
import com.cognibank.usermng.cognibankusersecurityquestionsmicroservice.repository.SecurityQuestionRepository;
import com.cognibank.usermng.cognibankusersecurityquestionsmicroservice.repository.UserAnswerRepository;
import com.cognibank.usermng.cognibankusersecurityquestionsmicroservice.service.UserAnswerService;
import com.cognibank.usermng.cognibankusersecurityquestionsmicroservice.service.exception.SecurityQuestionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAnswerServiceImpl implements UserAnswerService {
    @Autowired
    UserAnswerRepository userAnswerRepository;

    @Autowired
    SecurityQuestionRepository securityQuestionRepository;

    @Override
    public Long addAnswer(String userId, Long questionId, String answer) {
        SecurityQuestion securityQuestion = securityQuestionRepository.findById(questionId)
                .orElseThrow(SecurityQuestionNotFoundException::new);

        return userAnswerRepository.save(
                new UserAnswers()
                        .withUserId(userId)
                        .withQuestion(securityQuestion)
                        .withAnswer(answer)
        ).getId();
    }

    @Override
    public boolean checkAnswer(String userId, Long questionId, String answer) {
        return userAnswerRepository.findByUserIdAndQuestionIdAndAnswerIgnoreCase(userId, questionId, answer).isPresent();
    }

    @Override
    public List<SecurityQuestion> getUserQuestions(String userId) {
        List<UserAnswers> userAnswers =  userAnswerRepository.findByUserId(userId);
        List<SecurityQuestion> securityQuestions = userAnswers.stream()
                .map(UserAnswers::getQuestion)
                .collect(Collectors.toList());
        return securityQuestions;
    }
}
