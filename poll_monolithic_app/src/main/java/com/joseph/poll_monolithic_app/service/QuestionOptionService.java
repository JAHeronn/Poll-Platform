package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.QuestionOptionReqDto;
import com.joseph.poll_monolithic_app.dto.QuestionOptionResDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.Question;
import com.joseph.poll_monolithic_app.model.QuestionOption;
import com.joseph.poll_monolithic_app.repository.QuestionOptionRepo;
import com.joseph.poll_monolithic_app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionOptionService {

    private final QuestionOptionRepo questionOptionRepository;
    private final QuestionRepository questionRepository;

    public QuestionOptionResDto addOption(Long pollId, Long questionId, QuestionOptionReqDto optionDto) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        QuestionOption option = new QuestionOption();
        option.setOptionText(optionDto.getOptionText());
        option.setQuestion(question);

        QuestionOption savedOption = questionOptionRepository.save(option);

        QuestionOptionResDto optionResDto = new QuestionOptionResDto();
        optionResDto.setId(savedOption.getId());
        optionResDto.setOptionText(savedOption.getOptionText());

        return optionResDto;
    }
}
