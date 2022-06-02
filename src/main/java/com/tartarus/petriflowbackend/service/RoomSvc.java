package com.tartarus.petriflowbackend.service;

import com.tartarus.petriflowbackend.dto.RoomDto;
import org.springframework.stereotype.Service;

@Service
public interface RoomSvc {

    boolean existsByUrlPassword(String urlPassword);

    RoomDto getByUrlPassword(String urlPassword);

    RoomDto createNewRoom(String urlPassword, boolean isFormBuilderRoom);

    void updateRoom(RoomDto roomDto);
}
