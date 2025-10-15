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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    public PollResponseDto createPoll(PollRequestDto pollDto) {
        User mockUser = userRepository.findById(1L)
            .orElseThrow(() -> new ResourceNotFoundException("Mock user not found"));
        Tenant mockTenant = tenantRepository.findById(1L)
            .orElseThrow(() -> new ResourceNotFoundException("Mock tenant not found"));

        Poll poll = mapToEntity(pollDto, mockUser, mockTenant);
        Poll savedPoll = pollRepository.save(poll);

        return mapToDto(savedPoll);
    }

    public Poll mapToEntity(PollRequestDto pollDto, User creator, Tenant tenant) {
        Poll poll = new Poll();
        poll.setTitle(pollDto.getTitle());
        poll.setDescription(pollDto.getDescription());
        // will set currentUser and currentTenant when doing auth
        poll.setCreator(creator);
        poll.setTenant(tenant);

        return poll;
    }

    public PollResponseDto mapToDto(Poll poll) {
        PollResponseDto pollDto = new PollResponseDto();
        pollDto.setId(poll.getId());
        pollDto.setTitle(poll.getTitle());
        pollDto.setDescription(poll.getDescription());
        pollDto.setCreatorName(poll.getCreator().getFullName());
        pollDto.setCreatorUsername(poll.getCreator().getUsername());
        pollDto.setTenantName(poll.getTenant().getName());
        pollDto.setVisibility(poll.getVisibility());
        pollDto.setPollStatus(poll.getPollStatus());
        pollDto.setCreatedAt(poll.getCreatedAt());

        return pollDto;
    }

    public List<PollResponseDto> getAllPolls() {
        // temporary placeholder until currentTenant in auth is set
        Tenant mockTenant = tenantRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

        return pollRepository.findByTenant(mockTenant).stream()
                .map(this::mapToDto)
                .toList();
    }

    public PollResponseDto getPoll(Long id) {
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));

        return mapToDto(poll);
    }
}
