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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    public PollResponseDto createPoll(PollRequestDto pollDto) {
        Poll poll = mapToEntity(pollDto);
        // will have to do a check here that current user is set before saving to db
        // i.e. making sure poll.setCreator and poll.setTenant isn't null
        Poll savedPoll = pollRepository.save(poll);

        return mapToDto(savedPoll);
    }

    public Poll mapToEntity(PollRequestDto pollDto) {
        Poll poll = new Poll();
        poll.setTitle(pollDto.getTitle());
        poll.setDescription(pollDto.getDescription());
        // need to set currentUser and currentTenant here but have to do authorisation for it

        // temporary placeholder until auth is sorted
        User mockUser = userRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Mock user not found"));
        Tenant mockTenant = tenantRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Mock tenant not found"));

        poll.setCreator(mockUser);
        poll.setTenant(mockTenant);

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

}
