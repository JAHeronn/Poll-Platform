package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.PollRequestDto;
import com.joseph.poll_monolithic_app.dto.PollResponseDto;
import com.joseph.poll_monolithic_app.model.Poll;
import com.joseph.poll_monolithic_app.model.Tenant;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.repository.PollRepository;
import com.joseph.poll_monolithic_app.repository.TenantRepository;
import com.joseph.poll_monolithic_app.repository.UserRepository;
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
class PollServiceTest {

    @Mock
    private PollRepository pollRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TenantRepository tenantRepository;

    @InjectMocks
    private PollService pollService;

    @Test
    void createPoll_ShouldSavePoll() {
        Long userId = 1L;
        Long tenantId = 1L;

        User user = User.builder().id(userId)
                .username("JohnT")
                .fullName("John Turner")
                .email("John@example.com")
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Tenant tenant = Tenant.builder().id(tenantId).name("John's Space").build();
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenant));

        PollRequestDto requestDto = new PollRequestDto();
        requestDto.setTitle("What's your favourite colour?");
        requestDto.setDescription("A poll about colours");

        Poll savedPoll = Poll.builder().id(1L)
                .creator(user)
                .tenant(tenant)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .build();
        when(pollRepository.save(any(Poll.class))).thenReturn(savedPoll);

        PollResponseDto result = pollService.createPoll(requestDto);

        ArgumentCaptor<Poll> pollCaptor = ArgumentCaptor.forClass(Poll.class);
        verify(pollRepository).save(pollCaptor.capture());
        assertEquals("What's your favourite colour?", pollCaptor.getValue().getTitle());
        assertEquals("A poll about colours", pollCaptor.getValue().getDescription());
        assertEquals(user, pollCaptor.getValue().getCreator());
        assertEquals(tenant, pollCaptor.getValue().getTenant());

        assertNotNull(result);
        assertEquals(savedPoll.getId(), result.getId());
        assertEquals(savedPoll.getTitle(), result.getTitle());
        assertEquals(savedPoll.getDescription(), result.getDescription());
        assertEquals(savedPoll.getCreator().getFullName(), result.getCreatorName());
        assertEquals(savedPoll.getTenant().getName(), result.getTenantName());
    }
}