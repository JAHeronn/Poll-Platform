package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.PollRequestDto;
import com.joseph.poll_monolithic_app.dto.PollResponseDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

    private PollRequestDto createPollRequestDto() {
        PollRequestDto pollRequest = new PollRequestDto();
        pollRequest.setTitle("What's your favourite colour?");
        pollRequest.setDescription("A poll about colours");
        return pollRequest;
    }

    private User createMockUser() {
        return User.builder().id(1L)
                .username("JohnT")
                .fullName("John Turner")
                .email("John@example.com")
                .build();
    }

    @Test
    void createPoll_ShouldSavePoll() {
        User user = createMockUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Tenant tenant = Tenant.builder().id(1L).name("John's Space").build();
        when(tenantRepository.findById(tenant.getId())).thenReturn(Optional.of(tenant));

        PollRequestDto requestDto = createPollRequestDto();

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

    @Test
    void createPoll_ShouldThrowResourceNotFound_WhenUserIsMissing() {
        PollRequestDto pollRequest = createPollRequestDto();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> pollService.createPoll(pollRequest));
    }

    @Test
    void createPoll_ShouldThrowResourceNotFound_WhenTenantIsMissing() {
        PollRequestDto pollRequest = createPollRequestDto();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(tenantRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> pollService.createPoll(pollRequest));
    }

    @Test
    void getAllPolls_ShouldReturnAllPollsOfTenant_WhenTenantIsFound() {
        Tenant tenant = Tenant.builder().id(1L).name("John's Space").build();
        User user = createMockUser();
        Poll poll1 = Poll.builder().id(1L).title("Poll 1").tenant(tenant).creator(user).build();
        Poll poll2 = Poll.builder().id(2L).title("Poll 2").tenant(tenant).creator(user).build();

        when(tenantRepository.findById(1L)).thenReturn(Optional.of(tenant));
        when(pollRepository.findByTenant(tenant)).thenReturn(List.of(poll1, poll2));

        List<PollResponseDto> result = pollService.getAllPolls();

        assertEquals(2, result.size());
        assertEquals("Poll 1", result.getFirst().getTitle());
        assertEquals("Poll 2", result.get(1).getTitle());

        verify(tenantRepository).findById(1L);
        verify(pollRepository).findByTenant(tenant);
    }
}