package org.example.protobuf;

import org.example.model.*;
import org.example.service.ServiceException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProtoUtils {
    public static SwimmersProtobufs.Request createLoginRequest(User user){
        SwimmersProtobufs.User userDTO = SwimmersProtobufs.User.newBuilder().setEmail(user.getEmail()).setPassword(user.getPassword()).build();
        SwimmersProtobufs.Request request = SwimmersProtobufs.Request.newBuilder().setType(SwimmersProtobufs.Request.Type.Login).setUser(userDTO).build();
        return request;
    }

    public static SwimmersProtobufs.Request createLogoutRequest(User user){
        SwimmersProtobufs.User userDTO = SwimmersProtobufs.User.newBuilder().setEmail(user.getEmail()).setPassword(user.getPassword()).build();
        SwimmersProtobufs.Request request = SwimmersProtobufs.Request.newBuilder().setType(SwimmersProtobufs.Request.Type.Logout).setUser(userDTO).build();
        return request;
    }

    public static SwimmersProtobufs.Request createGetEventsRequest(){
        SwimmersProtobufs.Request request = SwimmersProtobufs.Request.newBuilder().setType(SwimmersProtobufs.Request.Type.GetEvents).build();
        return request;
    }

    public static SwimmersProtobufs.Request createGetParticipantsRequest(Event event){
        SwimmersProtobufs.Event eventproto = SwimmersProtobufs.Event.newBuilder().setId(event.getId()).setDistance(event.getDistance()).setStyle(event.getStyle()).build();
        SwimmersProtobufs.Request request = SwimmersProtobufs.Request.newBuilder().setType(SwimmersProtobufs.Request.Type.GetParticipants).setEvent(eventproto).build();
        return request;
    }

    public static SwimmersProtobufs.Request createAddSwimmerRequest(Swimmer swimmer){
       SwimmersProtobufs.Swimmer swimmerProto = SwimmersProtobufs.Swimmer.newBuilder().setFirstName(swimmer.getFirstName()).setLastName(swimmer.getLastName()).setBirthDate(swimmer.getBirthDate().toString()).build();
       SwimmersProtobufs.Request request = SwimmersProtobufs.Request.newBuilder().setType(SwimmersProtobufs.Request.Type.AddSwimmer).setSwimmer(swimmerProto).build();
       return request;
    }

    public static SwimmersProtobufs.Request createAddParticipationRequest2(Participation participation){
        SwimmersProtobufs.Swimmer swimmerProto = SwimmersProtobufs.Swimmer.newBuilder().setId(participation.getSwimmer().getId()).setFirstName(participation.getSwimmer().getFirstName()).setLastName(participation.getSwimmer().getLastName()).setBirthDate(participation.getSwimmer().getBirthDate().toString()).build();
        SwimmersProtobufs.Event eventProto = SwimmersProtobufs.Event.newBuilder().setId(participation.getEvent().getId()).setDistance(participation.getEvent().getDistance()).setStyle(participation.getEvent().getStyle()).build();
        SwimmersProtobufs.Participation participationProto = SwimmersProtobufs.Participation.newBuilder().setSwimmer(swimmerProto).setEvent(eventProto).build();
        SwimmersProtobufs.Request request = SwimmersProtobufs.Request.newBuilder().setType(SwimmersProtobufs.Request.Type.AddParticipation).setParticipation(participationProto).build();
        return request;
    }

    public static SwimmersProtobufs.Request createAddParticipationRequest(Swimmer swimmer, Event event){
        SwimmersProtobufs.Swimmer swimmerProto = SwimmersProtobufs.Swimmer.newBuilder().setId(swimmer.getId()).setFirstName(swimmer.getFirstName()).setLastName(swimmer.getLastName()).setBirthDate(swimmer.getBirthDate().toString()).build();
        SwimmersProtobufs.Event eventProto = SwimmersProtobufs.Event.newBuilder().setId(event.getId()).setDistance(event.getDistance()).setStyle(event.getStyle()).build();
        SwimmersProtobufs.Participation participationProto = SwimmersProtobufs.Participation.newBuilder().setSwimmer(swimmerProto).setEvent(eventProto).build();
        SwimmersProtobufs.Request request = SwimmersProtobufs.Request.newBuilder().setType(SwimmersProtobufs.Request.Type.AddParticipation).setParticipation(participationProto).build();
        return request;
    }

    public static SwimmersProtobufs.Request createFindLastSwimmerRequest(){
        SwimmersProtobufs.Request request = SwimmersProtobufs.Request.newBuilder().setType(SwimmersProtobufs.Request.Type.FindLastSwimmer).build();
        return request;
    }

    public static SwimmersProtobufs.Request createUpdateRequest(){
        SwimmersProtobufs.Request request = SwimmersProtobufs.Request.newBuilder().setType(SwimmersProtobufs.Request.Type.Update).build();
        return request;
    }

    public static SwimmersProtobufs.Response createOkResponse() {
        SwimmersProtobufs.Response response = SwimmersProtobufs.Response.newBuilder().setType(SwimmersProtobufs.Response.Type.Ok).build();
        return response;
    }

    public static SwimmersProtobufs.Response createErrorResponse() {
        SwimmersProtobufs.Response response = SwimmersProtobufs.Response.newBuilder().setType(SwimmersProtobufs.Response.Type.Error).build();
        return response;
    }

    public static SwimmersProtobufs.Response createLoginResponse(User user) {
        SwimmersProtobufs.User userDTO = SwimmersProtobufs.User.newBuilder().setId(user.getId()).setFirstName(user.getFirstName()).setLastName(user.getLastName()).setEmail(user.getEmail()).setPassword(user.getPassword()).build();
        SwimmersProtobufs.Response response = SwimmersProtobufs.Response.newBuilder().setType(SwimmersProtobufs.Response.Type.Login).build();
        return response;
    }

    public static SwimmersProtobufs.Response createGetEventsResponse(EventDTO[] events) {
        SwimmersProtobufs.Response.Builder response=SwimmersProtobufs.Response.newBuilder()
                .setType(SwimmersProtobufs.Response.Type.GetEvents);
        for (EventDTO event: events){
            SwimmersProtobufs.EventDTO eventProto=SwimmersProtobufs.EventDTO.newBuilder().setId(event.getId()).setDistance(event.getDistance()).setStyle(event.getStyle()).setNumberSwimmers(event.getNrSwimmers()).build();
            response.addEvents(eventProto);
        }
        return response.build();
    }

    public static SwimmersProtobufs.Response createGetParticipantsResponse(ParticipantDTO[] participants) {
        SwimmersProtobufs.Response.Builder response=SwimmersProtobufs.Response.newBuilder()
                .setType(SwimmersProtobufs.Response.Type.GetParticipants);
        for (ParticipantDTO participant: participants){
            SwimmersProtobufs.ParticipantDTO participantProto=SwimmersProtobufs.ParticipantDTO.newBuilder().setFirstName(participant.getFirstName()).setLastName(participant.getLastName()).setAge(participant.getAge()).setEvents(participant.getEvents()).build();
            response.addParticipants(participantProto);
        }
        return response.build();
    }

    public static SwimmersProtobufs.Response createAddSwimmerResponse() {
        SwimmersProtobufs.Response response = SwimmersProtobufs.Response.newBuilder().setType(SwimmersProtobufs.Response.Type.AddSwimmer).build();
        return response;
    }

    public static SwimmersProtobufs.Response createAddParticipationResponse() {
        SwimmersProtobufs.Response response = SwimmersProtobufs.Response.newBuilder().setType(SwimmersProtobufs.Response.Type.AddParticipation).build();
        return response;
    }

    public static SwimmersProtobufs.Response createFindLastSwimmerResponse(Swimmer swimmer) {
        SwimmersProtobufs.Swimmer swimmerProto = SwimmersProtobufs.Swimmer.newBuilder().setId(swimmer.getId()).setFirstName(swimmer.getFirstName()).setLastName(swimmer.getLastName()).setBirthDate(swimmer.getBirthDate().toString()).build();
        SwimmersProtobufs.Response response = SwimmersProtobufs.Response.newBuilder().setType(SwimmersProtobufs.Response.Type.FindLastSwimmer).setSwimmer(swimmerProto).build();
        return response;
    }

    public static User getUser(SwimmersProtobufs.Request request){
        User user=new User();
        user.setId(request.getUser().getId());
        user.setFirstName(request.getUser().getFirstName());
        user.setLastName(request.getUser().getLastName());
        user.setEmail(request.getUser().getEmail());
        user.setPassword(request.getUser().getPassword());
        return user;
    }

    public static Swimmer getLastSwimmer(SwimmersProtobufs.Response response){
        Swimmer swimmer=new Swimmer();
        swimmer.setId(response.getSwimmer().getId());
        swimmer.setFirstName(response.getSwimmer().getFirstName());
        swimmer.setLastName(response.getSwimmer().getLastName());
        String birth_string = response.getSwimmer().getBirthDate();

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(birth_string, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String output = dateTime.format(outputFormatter);
        LocalDateTime outputDateTime = LocalDateTime.parse(output, outputFormatter);


        //LocalDateTime birthDate = LocalDateTime.parse(birth_string);
        swimmer.setBirthDate(outputDateTime);
        return swimmer;
    }

    public static String getError(SwimmersProtobufs.Response response){
        String errorMessage=response.getError();
        return errorMessage;
    }

    public static EventDTO[] getAllEvents(SwimmersProtobufs.Response response) throws ServiceException {
        EventDTO[] events = new EventDTO[response.getEventsCount()];
        for(int i=0; i<response.getEventsCount();i++){
            SwimmersProtobufs.EventDTO eventDTO=response.getEvents(i);
            EventDTO event = new EventDTO();
            event.setId(eventDTO.getId());
            event.setDistance(eventDTO.getDistance());
            event.setStyle(eventDTO.getStyle());
            event.setNrSwimmers(eventDTO.getNumberSwimmers());
            events[i]=event;
        }
        return events;
    }

    public static ParticipantDTO[] getParticipants(SwimmersProtobufs.Response response) throws ServiceException {
        ParticipantDTO[] participants = new ParticipantDTO[response.getParticipantsCount()];
        for(int i=0; i<response.getParticipantsCount();i++){
            SwimmersProtobufs.ParticipantDTO participantDTO=response.getParticipants(i);
            ParticipantDTO participant = new ParticipantDTO();
            participant.setFirstName(participantDTO.getFirstName());
            participant.setLastName(participantDTO.getLastName());
            participant.setAge(participantDTO.getAge());
            participant.setEvents(participantDTO.getEvents());
            participants[i]=participant;
        }
        return participants;
    }

}
