package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.PollRequestDto;
import com.joseph.poll_monolithic_app.dto.PollSubmissionReqDto;
import com.joseph.poll_monolithic_app.dto.PollSubmissionResDto;
import com.joseph.poll_monolithic_app.dto.QuestionAnswerReqDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.*;
import com.joseph.poll_monolithic_app.model.enums.QuestionType;
import com.joseph.poll_monolithic_app.repository.PollRepository;
import com.joseph.poll_monolithic_app.repository.PollSubmissionRepo;
import com.joseph.poll_monolithic_app.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PollSubmissionServiceTest {
    @Mock
    private PollSubmissionRepo pollSubmissionRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private PollRepository pollRepository;

    @InjectMocks
    private PollSubmissionService pollSubmissionService;

    private User createMockUser(String username, String fullName, String email) {
        return User.builder().id(1L)
                .username(username)
                .fullName(fullName)
                .email(email)
                .build();
    }

    private Question createQuestion(Poll poll) {
        return Question.builder().id(1L)
                .poll(poll)
                .questionText("What's your favourite colour?")
                .type(QuestionType.TEXT)
                .build();
    }

    @Test
    void submitPoll_ShouldSavePoll_WhenPollIsFound() {
        Tenant tenant = Tenant.builder().id(1L).name("John's Space").build();
        User creator = createMockUser("JohnT", "John Turner", "John@example.com");
        Poll poll = Poll.builder().id(1L).title("Poll").tenant(tenant).creator(creator).build();

        when(pollRepository.findById(poll.getId())).thenReturn(Optional.of(poll));

        User responder = createMockUser("SamT", "Sam Turner", "Sam@example.com");
        Question question = createQuestion(poll);

        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        QuestionAnswerReqDto answer1 = new QuestionAnswerReqDto();
        answer1.setAnswer("Blue");
        answer1.setQuestionId(question.getId());

        PollSubmissionReqDto submissionReqDto = new PollSubmissionReqDto();
        submissionReqDto.setAnswers(List.of(answer1));

        List<QuestionAnswer> answers = submissionReqDto.getAnswers()
                .stream()
                .map(dto -> QuestionAnswer.builder()
                        .answer(dto.getAnswer())
                        .question(question)
                        .build())
                .toList();

        PollSubmission savedSubmission = PollSubmission.builder().id(1L)
                .poll(poll)
                .answers(answers)
                .responder(responder)
                .build();

        when(pollSubmissionRepository.save(any(PollSubmission.class))).thenReturn(savedSubmission);

        PollSubmissionResDto result = pollSubmissionService.submitPoll(submissionReqDto, poll.getId(), responder);

        ArgumentCaptor<PollSubmission> captor = ArgumentCaptor.forClass(PollSubmission.class);
        verify(pollSubmissionRepository).save(captor.capture());
        assertEquals("Blue", captor.getValue().getAnswers().getFirst().getAnswer());
        assertEquals(question.getId(), captor.getValue().getAnswers().getFirst().getQuestion().getId());

        assertNotNull(result);
        assertEquals(savedSubmission.getPoll().getId(), result.getPollId());
        assertEquals(savedSubmission.getAnswers().getFirst().getAnswer(), result.getAnswers().getFirst().getAnswer());
        assertEquals(savedSubmission.getResponder().getFullName(), result.getResponderName());
    }

    @Test
    void submitPoll_ShouldThrowResourceNotFound_WhenPollIsMissing() {
        when(pollRepository.findById(1L)).thenReturn(Optional.empty());

        User responder = createMockUser("SamT", "Sam Turner", "Sam@example.com");

        PollSubmissionReqDto pollSubmissionReqDto = new PollSubmissionReqDto();

        assertThrows(ResourceNotFoundException.class,
                () -> pollSubmissionService.submitPoll(pollSubmissionReqDto, 1L, responder));
    }
}