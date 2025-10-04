package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.QuestionOptionDto;
import com.joseph.poll_monolithic_app.model.Question;
import com.joseph.poll_monolithic_app.model.QuestionOption;
import com.joseph.poll_monolithic_app.repository.QuestionOptionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionOptionService {

    private final QuestionOptionRepo questionOptionRepository;

    public QuestionOption addOption(QuestionOptionDto optionDto, Question question) {
        QuestionOption option = new QuestionOption();
        option.setOptionText(optionDto.getOptionText());
        option.setQuestion(question);

        return questionOptionRepository.save(option);
    }
}
