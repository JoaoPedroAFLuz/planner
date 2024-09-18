package br.com.joaopedroafluz.timely.link;

import org.springframework.stereotype.Component;

@Component
public class LinkConverter {

    public LinkDTO entityToDTO(Link link) {
        return new LinkDTO(
                link.getActivity().getCode(),
                link.getCode(),
                link.getTitle(),
                link.getUrl());
    }

}
