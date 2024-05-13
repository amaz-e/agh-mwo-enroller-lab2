package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {
    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Collection<Meeting> getAllMeetings() {
        return meetingService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Meeting getMeeting(@PathVariable("id") long id){
        return meetingService.getMeetingbyID(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
        if (meetingService.getMeetingbyID(meeting.getId()) != null) {
            return new ResponseEntity<String>(
                    "Unable to create. A meeting with id " + meeting.getId() + " already exist.",
                    HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@PathVariable("id") String login, @RequestBody Meeting updatedMeeting) {
        Meeting meeting = meetingService.getMeetingbyID(updatedMeeting.getId());
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.update(updatedMeeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") Meeting meetingId) {
        Meeting meeting = meetingService.getMeetingbyID(meetingId.getId());
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(meeting);
        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id){
        Collection<Participant> participants = meetingService.getMeetingbyID(id).getParticipants();
        return new ResponseEntity<Collection<Participant>> (participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addMeetingParticipant(@PathVariable("id") long id,@RequestParam(value = "login", defaultValue = "") String login){
        Meeting meeting = meetingService.getMeetingbyID(id);
        Participant newParticipant;
        if (login == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        newParticipant = participantService.findByLogin(login);
        if (newParticipant == null)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meeting.addParticipant(newParticipant);

        Collection<Participant> participants = meeting.getParticipants();
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeetingParticipants(@PathVariable("id") long id, @PathVariable("login") String login ){
        Meeting meeting = meetingService.getMeetingbyID(id);
        meeting.removeParticipant(participantService.findByLogin(login));
        return new ResponseEntity<Participant>(HttpStatus.OK);
    }
}
