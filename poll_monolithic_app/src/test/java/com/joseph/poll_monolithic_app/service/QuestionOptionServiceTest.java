package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.QuestionOptionReqDto;
import com.joseph.poll_monolithic_app.dto.QuestionOptionResDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.Poll;
import com.joseph.poll_monolithic_app.model.Question;
import com.joseph.poll_monolithic_app.model.QuestionOption;
import com.joseph.poll_monolithic_app.model.enums.QuestionType;
import com.joseph.poll_monolithic_app.repository.QuestionOptionRepo;
import com.joseph.poll_monolithic_app.repository.QuestionRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionOptionServiceTest {
    @Mock
    private QuestionOptionRepo questionOptionRepo;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionOptionService questionOptionService;

    @Test
    void addOption_ShouldSaveOption_WhenQuestionIsFound() throws BadRequestException {
        Poll poll = Poll.builder().id(1L).title("Poll").build();
        Question question = Question.builder().id(1L)
                .poll(poll)
                .questionText("What's your favourite colour?")
                .type(QuestionType.TEXT)
                .build();

        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        QuestionOptionReqDto optionReqDto = new QuestionOptionReqDto();
        optionReqDto.setOptionText("Blue");

        QuestionOption questionOption = QuestionOption.builder()
                .optionText(optionReqDto.getOptionText())
                .id(1L)
                .question(question)
                .build();

        when(questionOptionRepo.save(any(QuestionOption.class))).thenReturn(questionOption);

        QuestionOptionResDto result = questionOptionService.addOption(poll.getId(), question.getId(), optionReqDto);

        ArgumentCaptor<QuestionOption> optionCaptor = ArgumentCaptor.forClass(QuestionOption.class);
        verify(questionOptionRepo).save(optionCaptor.capture());
        assertEquals("Blue", optionCaptor.getValue().getOptionText());

        assertEquals(questionOption.getId(), result.getId());
        assertEquals(questionOption.getOptionText(), result.getOptionText());
    }

    @Test
    void addOption_ShouldThrowResourceNotFoundException_WhenQuestionIsMissing() {
        QuestionOptionReqDto optionReqDto = new QuestionOptionReqDto();
        optionReqDto.setOptionText("Blue");

        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> questionOptionService.addOption(1L, 1L, optionReqDto));
    }
}