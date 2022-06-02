package com.tartarus.petriflowbackend.dto;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class RoomDto {

    private String password;

    private Set<String> users = new HashSet<>();

    private Map<String, DataDto> modelObjectsMap = new LinkedHashMap<>();

    private boolean formBuilderRoom = false;

    public RoomDto() {
    }

    public RoomDto(String password) {
        this(password, false);
    }

    public RoomDto(String password, boolean formBuilderRoom) {
        this.password = password;
        this.formBuilderRoom = formBuilderRoom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public Map<String, DataDto> getModelObjectsMap() {
        return modelObjectsMap;
    }

    public void setModelObjectsMap(Map<String, DataDto> modelObjectsMap) {
        this.modelObjectsMap = modelObjectsMap;
    }

    public boolean isFormBuilderRoom() {
        return formBuilderRoom;
    }

    public void setFormBuilderRoom(boolean formBuilderRoom) {
        this.formBuilderRoom = formBuilderRoom;
    }
}