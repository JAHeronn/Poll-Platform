package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.QuestionAnswerReqDto;
import com.joseph.poll_monolithic_app.dto.QuestionAnswerResDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.*;
import com.joseph.poll_monolithic_app.model.enums.QuestionType;
import com.joseph.poll_monolithic_app.repository.PollSubmissionRepo;
import com.joseph.poll_monolithic_app.repository.QuestionAnswerRepo;
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
class QuestionAnswerServiceTest {
    @Mock
    private QuestionAnswerRepo questionAnswerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private PollSubmissionRepo pollSubmissionRepo;

    @InjectMocks
    private QuestionAnswerService questionAnswerService;

    private User createMockUser(String username, String fullName, String email) {
        return User.builder().id(1L)
                .username(username)
                .fullName(fullName)
                .email(email)
                .build();
    }

    private PollSubmission createPollSubmission() {
        Tenant tenant = Tenant.builder().id(1L).name("John's Space").build();
        User creator = createMockUser("JTurner","John Turner", "john@example.com");
        Poll poll = Poll.builder().id(1L).title("Poll").tenant(tenant).creator(creator).build();

        User responder = createMockUser("SamT", "Sam Turner", "Sam@example.com");

        return PollSubmission.builder().id(1L).poll(poll).responder(responder).build();
    }

    private QuestionAnswerReqDto createQuestionAnswerReqDto() {
        QuestionAnswerReqDto answerReqDto = new QuestionAnswerReqDto();
        answerReqDto.setQuestionId(1L);
        answerReqDto.setAnswer("Blue");

        return answerReqDto;
    }

    @Test
    void addAnswer_ShouldSaveAnswer() throws BadRequestException {
        PollSubmission pollSubmission = createPollSubmission();

        when(pollSubmissionRepo.findById(pollSubmission.getId())).thenReturn(Optional.of(pollSubmission));

        Question question = Question.builder().id(1L)
                .poll(pollSubmission.getPoll())
                .questionText("What's your favourite colour?")
                .type(QuestionType.TEXT)
                .build();

        QuestionAnswerReqDto answerReqDto = new QuestionAnswerReqDto();
        answerReqDto.setQuestionId(question.getId());
        answerReqDto.setAnswer("Blue");

        when(questionRepository.findById(answerReqDto.getQuestionId())).thenReturn(Optional.of(question));

        QuestionAnswer savedAnswer = QuestionAnswer.builder().id(1L)
                .question(question)
                .response(pollSubmission)
                .answer(answerReqDto.getAnswer())
                .build();

        when(questionAnswerRepository.save(any(QuestionAnswer.class))).thenReturn(savedAnswer);

        QuestionAnswerResDto result = questionAnswerService.addAnswer(pollSubmission.getPoll().getId(), pollSubmission.getId(), answerReqDto);

        ArgumentCaptor<QuestionAnswer> captor = ArgumentCaptor.forClass(QuestionAnswer.class);
        verify(questionAnswerRepository).save(captor.capture());
        assertEquals("Blue", captor.getValue().getAnswer());
        assertEquals("What's your favourite colour?", captor.getValue().getQuestion().getQuestionText());

        assertNotNull(result);
        assertEquals(savedAnswer.getId(), result.getId());
        assertEquals(savedAnswer.getAnswer(), result.getAnswer());
        assertEquals(savedAnswer.getQuestion().getId(), result.getQuestionId());
    }

    @Test
    void addAnswer_ShouldThrowResourceNotFound_WhenPollSubmissionIsMissing() {
        QuestionAnswerReqDto answerReqDto = createQuestionAnswerReqDto();

        when(pollSubmissionRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> questionAnswerService.addAnswer(1L, 1L, answerReqDto));
    }

    @Test
    void addAnswer_ShouldThrowResourceNotFound_WhenQuestionIsMissing() {
        QuestionAnswerReqDto answerReqDto = createQuestionAnswerReqDto();

        PollSubmission pollSubmission = createPollSubmission();

        when(pollSubmissionRepo.findById(1L)).thenReturn(Optional.of(pollSubmission));
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> questionAnswerService.addAnswer(1L, 1L, answerReqDto));
    }

    @Test
    void addAnswer_ShouldThrowBadRequest_WhenPollOfPollSubmissionDoesNotMatch() {
        QuestionAnswerReqDto answerReqDto = createQuestionAnswerReqDto();
        PollSubmission pollSubmission = createPollSubmission();
        Question question = Question.builder().id(1L)
                .poll(pollSubmission.getPoll())
                .questionText("What's your favourite colour?")
                .type(QuestionType.TEXT)
                .build();

        when(pollSubmissionRepo.findById(1L)).thenReturn(Optional.of(pollSubmission));
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        assertThrows(BadRequestException.class,
                () -> questionAnswerService.addAnswer(2L, 1L, answerReqDto));
    }

    @Test
    void addAnswer_ShouldThrowBadRequest_WhenPollOfQuestionDoesNotMatch() {
        QuestionAnswerReqDto answerReqDto = createQuestionAnswerReqDto();
        Tenant tenant = Tenant.builder().id(1L).name("John's Space").build();
        User creator = createMockUser("JTurner","John Turner", "john@example.com");

        PollSubmission pollSubmission = createPollSubmission();

        Poll pollTwo = Poll.builder().id(2L).title("Poll 2").tenant(tenant).creator(creator).build();

        Question question = Question.builder().id(1L)
                .poll(pollTwo)
                .questionText("What's your favourite colour?")
                .type(QuestionType.TEXT)
                .build();

        when(pollSubmissionRepo.findById(1L)).thenReturn(Optional.of(pollSubmission));
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        assertThrows(BadRequestException.class,
                () -> questionAnswerService.addAnswer(1L, 1L, answerReqDto));
    }
}