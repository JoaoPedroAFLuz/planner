package br.com.joaopedroafluz.timely.tripParticipant;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripParticipantConverter {

    private final ModelMapper modelMapper;


    public TripParticipantDTO entityToDTO(TripParticipant tripParticipant) {
        return modelMapper.map(tripParticipant, TripParticipantDTO.class);
    }

}
