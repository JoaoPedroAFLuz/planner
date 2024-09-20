package br.com.joaopedroafluz.timely.link;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkConverter {

    private final ModelMapper modelMapper;


    public LinkDTO entityToDTO(Link link) {
        return modelMapper.map(link, LinkDTO.class);
    }

}
