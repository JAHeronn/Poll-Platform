package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.PollRequestDto;
import com.joseph.poll_monolithic_app.dto.PollResponseDto;
import com.joseph.poll_monolithic_app.model.Poll;
import com.joseph.poll_monolithic_app.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;

    public PollResponseDto createPoll(PollRequestDto pollDto) {
        Poll poll = mapToEntity(pollDto);
        // will have to do a check here that current user is set before saving to db
        Poll savedPoll = pollRepository.save(poll);

        return mapToDto(savedPoll);
    }

    public Poll mapToEntity(PollRequestDto pollDto) {
        Poll poll = new Poll();
        poll.setTitle(pollDto.getTitle());
        poll.setDescription(pollDto.getDescription());
        // need to set currentUser and currentTenant here but have to do authorisation for it

        return poll;
    }

    public PollResponseDto mapToDto(Poll poll) {
        PollResponseDto pollDto = new PollResponseDto();
        pollDto.setId(poll.getId());
        pollDto.setTitle(poll.getTitle());
        pollDto.setDescription(poll.getDescription());
        pollDto.setCreatorName(poll.getCreator().getFullName());
        pollDto.setCreatorUserName(poll.getCreator().getUserName());
        pollDto.setTenantName(poll.getTenant().getName());
        pollDto.setVisibility(poll.getVisibility());
        pollDto.setPollStatus(poll.getPollStatus());
        pollDto.setCreatedAt(poll.getCreatedAt());

        return pollDto;
    }

}
