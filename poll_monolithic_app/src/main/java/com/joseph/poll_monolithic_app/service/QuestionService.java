package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.QuestionOptionResDto;
import com.joseph.poll_monolithic_app.dto.QuestionRequestDto;
import com.joseph.poll_monolithic_app.dto.QuestionResponseDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.Poll;
import com.joseph.poll_monolithic_app.model.Question;
import com.joseph.poll_monolithic_app.model.QuestionOption;
import com.joseph.poll_monolithic_app.repository.PollRepository;
import com.joseph.poll_monolithic_app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final PollRepository pollRepository;

    public QuestionResponseDto addQuestion(Long pollId, QuestionRequestDto questionDto) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));

        Question question = mapToEntity(questionDto);
        question.setPoll(poll);
        Question savedQuestion = questionRepository.save(question);

        return mapToDto(savedQuestion);
    }

    public Question mapToEntity(QuestionRequestDto questionDto) {
        Question question = new Question();
        question.setQuestionText(questionDto.getQuestionText());
        question.setType(questionDto.getType());

        if (questionDto.getOptions() != null) {
            List<QuestionOption> options = questionDto.getOptions()
                    .stream()
                    .map(optionDto -> {
                        QuestionOption option = new QuestionOption();
                        option.setOptionText(optionDto.getOptionText());
                        option.setQuestion(question);
                        return option;
                    })
                    .toList();

            question.setOptions(options);
        }

        return question;
    }

    public QuestionResponseDto mapToDto(Question question) {
        QuestionResponseDto questionDto = new QuestionResponseDto();
        questionDto.setId(question.getId());
        questionDto.setPollId(question.getPoll().getId());
        questionDto.setQuestionText(question.getQuestionText());
        questionDto.setType(question.getType());

        List<QuestionOptionResDto> optionDtos = question.getOptions()
                .stream()
                .map(option -> {
                    QuestionOptionResDto optionDto = new QuestionOptionResDto();
                    optionDto.setId(option.getId());
                    optionDto.setOptionText(option.getOptionText());
                    return optionDto;
                })
                .toList();

        questionDto.setOptions(optionDtos);

        return questionDto;

    }

    public List<QuestionResponseDto> getQuestion(Long pollId, Long questionId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));

        return questionRepository.findById(questionId).stream()
                .map(this::mapToDto)
                .toList();
    }
}
