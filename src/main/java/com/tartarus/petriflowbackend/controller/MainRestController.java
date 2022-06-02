package com.tartarus.petriflowbackend.controller;

import com.tartarus.petriflowbackend.response.NewRoomResponse;
import com.tartarus.petriflowbackend.service.RoomSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MainRestController {

    private final RoomSvc roomSvc;

    @Autowired
    public MainRestController(RoomSvc roomSvc) {
        this.roomSvc = roomSvc;
    }

    @GetMapping("/generate-room")
    public NewRoomResponse generateRoom() {

        String roomId;

        do {
            roomId = UUID.randomUUID().toString();

        } while (roomSvc.existsByUrlPassword(roomId)); //ugly and probably necessary, but for sure

        return new NewRoomResponse(roomId);
    }

}
