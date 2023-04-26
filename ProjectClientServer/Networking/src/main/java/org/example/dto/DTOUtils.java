package org.example.dto;

import org.example.model.Event;

public class DTOUtils {
    public static EventDTO getDTO(Event event) {
        return null;
    }
    public static EventDTO[] getDTO(Event[] events) {
        EventDTO[] frDTO = new EventDTO[events.length];
        for(int i=0; i<events.length; i++)
            frDTO[i] = getDTO(events[i]);
        return frDTO;
    }
}
