package br.com.joaopedroafluz.timely.configs;

import br.com.joaopedroafluz.timely.tripParticipant.TripParticipant;
import br.com.joaopedroafluz.timely.tripParticipant.TripParticipantDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(TripParticipant.class, TripParticipantDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getUser().getCode(), TripParticipantDTO::setCode);
            mapper.map(src -> src.getUser().getName(), TripParticipantDTO::setName);
            mapper.map(src -> src.getUser().getEmail(), TripParticipantDTO::setEmail);
            mapper.map(src -> src.getTrip().getCode(), TripParticipantDTO::setTripCode);
        });

        return modelMapper;
    }

}
