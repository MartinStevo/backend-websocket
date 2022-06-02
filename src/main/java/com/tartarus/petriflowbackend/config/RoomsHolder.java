package com.tartarus.petriflowbackend.config;

import com.tartarus.petriflowbackend.dto.RoomDto;

import java.util.HashMap;
import java.util.Map;

public class RoomsHolder {

    /**
     * Key = Room url password
     * Value = Room data
     */
    private Map<String, RoomDto> roomsMap = new HashMap<>();

    /**
     * Key = simpSessionId
     * Value = Room url password
     */
    private Map<String, String> simpSessionIdRoomPasswordMap = new HashMap<>();

    public RoomsHolder() {

    }

    public boolean containsRoom(String roomPassword) {
        return roomsMap.containsKey(roomPassword);
    }

    public RoomDto getRoom(String roomPassword) {
        return roomsMap.get(roomPassword);
    }

    public RoomDto addRoom(RoomDto roomDto) {
        roomsMap.put(roomDto.getPassword(), roomDto);
        return roomDto;
    }

    public void removeRoom(RoomDto roomDto) {
        roomsMap.remove(roomDto.getPassword());
    }

    public RoomDto signUserIntoRoom(String userName, String roomPassword, String simpSessionId) {
        RoomDto roomDto = roomsMap.get(roomPassword);
        roomDto.getUsers().add(userName);

        simpSessionIdRoomPasswordMap.put(simpSessionId, roomDto.getPassword());

        return roomDto;
    }

    public RoomDto removeUserFromRoomBySimpSessionId(String userName, String simpSessionId) {
        String roomPassword = simpSessionIdRoomPasswordMap.get(simpSessionId);
        RoomDto roomDto = roomsMap.get(roomPassword);
        roomDto.getUsers().remove(userName);

        simpSessionIdRoomPasswordMap.remove(simpSessionId);

        return roomDto;
    }

    public Map<String, RoomDto> getRoomsMap() {
        return roomsMap;
    }

    public void setRoomsMap(Map<String, RoomDto> roomsMap) {
        this.roomsMap = roomsMap;
    }

    public Map<String, String> getSimpSessionIdRoomPasswordMap() {
        return simpSessionIdRoomPasswordMap;
    }

    public void setSimpSessionIdRoomPasswordMap(Map<String, String> simpSessionIdRoomPasswordMap) {
        this.simpSessionIdRoomPasswordMap = simpSessionIdRoomPasswordMap;
    }
}
