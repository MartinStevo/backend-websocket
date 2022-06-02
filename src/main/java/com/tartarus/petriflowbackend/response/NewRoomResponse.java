package com.tartarus.petriflowbackend.response;

public class NewRoomResponse {

    private String roomId;

    public NewRoomResponse() {
    }

    public NewRoomResponse(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
