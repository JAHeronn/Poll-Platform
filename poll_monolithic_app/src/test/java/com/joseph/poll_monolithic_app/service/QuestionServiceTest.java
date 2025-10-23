package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.QuestionOptionReqDto;
import com.joseph.poll_monolithic_app.dto.QuestionOptionResDto;
import com.joseph.poll_monolithic_app.dto.QuestionRequestDto;
import com.joseph.poll_monolithic_app.dto.QuestionResponseDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.Poll;
import com.joseph.poll_monolithic_app.model.Question;
import com.joseph.poll_monolithic_app.model.QuestionOption;
import com.joseph.poll_monolithic_app.model.enums.QuestionType;
import com.joseph.poll_monolithic_app.repository.PollRepository;
import com.joseph.poll_monolithic_app.repository.QuestionRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.prefs.BackingStoreException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private PollRepository pollRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void addQuestion_ShouldSaveQuestionToPoll() {
        Long pollId = 1L;
        Poll poll = Poll.builder().id(pollId).title("Favourite Colours").build();
        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));

        QuestionRequestDto requestDto = new QuestionRequestDto();
        requestDto.setQuestionText("What is your favourite colour?");
        requestDto.setType(QuestionType.TEXT);

        QuestionOptionReqDto option1 = new QuestionOptionReqDto();
        option1.setOptionText("Blue");
        QuestionOptionReqDto option2 = new QuestionOptionReqDto();
        option2.setOptionText("Red");

        requestDto.setOptions(List.of(option1,option2));
        List<QuestionOption> options = requestDto.getOptions()
                .stream()
                .map(dto -> QuestionOption.builder()
                        .optionText(dto.getOptionText())
                        .build())
                .toList();

        Question savedQuestion = Question.builder().poll(poll)
                .id(1L)
                .questionText(requestDto.getQuestionText())
                .type(requestDto.getType())
                .options(options)
                .build();

        when(questionRepository.save(any(Question.class))).thenReturn(savedQuestion);

        QuestionResponseDto result = questionService.addQuestion(pollId, requestDto);

        ArgumentCaptor<Question> questionCaptor = ArgumentCaptor.forClass(Question.class);
        verify(questionRepository).save(questionCaptor.capture());
        assertEquals("What is your favourite colour?", questionCaptor.getValue().getQuestionText());
        assertEquals(QuestionType.TEXT, questionCaptor.getValue().getType());
        assertEquals("Blue", questionCaptor.getValue().getOptions().getFirst().getOptionText());

        assertNotNull(result);
        assertEquals(savedQuestion.getPoll().getId(), result.getPollId());
        assertEquals(savedQuestion.getId(), result.getId());
        assertEquals(savedQuestion.getType(), result.getType());

        List<String> expectedOptionText = savedQuestion.getOptions().stream()
                .map(QuestionOption::getOptionText).toList();

        List<String> actualOptionText = result.getOptions().stream()
                .map(QuestionOptionResDto::getOptionText).toList();

        assertEquals(expectedOptionText, actualOptionText);
    }

    @Test
    void getQuestion_ShouldReturnQuestion_WhenPollMatches() throws Exception {
        Long pollId = 1L;
        Long questionId = 10L;

        Poll poll = Poll.builder().id(pollId).build();
        Question question = Question.builder().id(questionId)
                .poll(poll)
                .questionText("Test?")
                .build();

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        QuestionResponseDto result = questionService.getQuestion(pollId, questionId);

        assertEquals("Test?", result.getQuestionText());
        assertEquals(question.getId(), result.getId());
        verify(questionRepository).findById(questionId);
    }

    @Test
    void getQuestion_ShouldThrowBadRequest_WhenPollDoesNotMatch() {
        Long pollId = 1L;
        Long questionId = 10L;

        Poll otherPoll = Poll.builder().id(2L).build();
        Question question = Question.builder().id(questionId)
                .poll(otherPoll)
                .questionText("Test?")
                .build();

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        assertThrows(BadRequestException.class,
                () -> questionService.getQuestion(pollId, questionId));
    }

    @Test
    void getQuestion_ShouldThrowResourceNotFound_WhenQuestionIsMissing() {
        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> questionService.getQuestion(1L, 100L));
    }
}