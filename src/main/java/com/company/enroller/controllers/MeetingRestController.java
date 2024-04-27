package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {
    @Autowired
    MeetingService meetingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Collection<Meeting> getAllMeetings() {
        return meetingService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Meeting getMeeting(@PathVariable("id") long id){
        return meetingService.getMeetingbyID(id);
    }

}
