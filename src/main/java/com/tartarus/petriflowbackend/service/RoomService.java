package com.tartarus.petriflowbackend.service;

import com.tartarus.petriflowbackend.dto.DataDto;
import com.tartarus.petriflowbackend.dto.RoomDto;
import com.tartarus.petriflowbackend.entity.RoomEntity;
import com.tartarus.petriflowbackend.model.PetriNet;
import com.tartarus.petriflowbackend.model.PetriNetObject;
import com.tartarus.petriflowbackend.repository.RoomRepository;
import com.tartarus.petriflowbackend.util.CloneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Collection;

@Service
public class RoomService implements RoomSvc {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public boolean existsByUrlPassword(String urlPassword) {
        return roomRepository.existsByUrlPassword(urlPassword);
    }

    @Override
    public RoomDto getByUrlPassword(String urlPassword) {
        RoomEntity roomEntity = roomRepository.getByUrlPassword(urlPassword);
        PetriNet petriNet = null;
        try {
            petriNet = (PetriNet) CloneUtil.deserialize(new ByteArrayInputStream(roomEntity.getData()));
        } catch (Exception e) {
            throw new RuntimeException("Cannot deserialize roomEntity with id=" + roomEntity.getId());
        }

        RoomDto roomDto = new RoomDto(urlPassword);
        for (PetriNetObject petriNetObject : petriNet.getPetriNetObjects()) {
            DataDto dataDto = new DataDto();
            dataDto.setObjectID(petriNetObject.getId());
            dataDto.setObjectType(petriNetObject.getObjectType());
            dataDto.setObject(petriNetObject.getObject());

            roomDto.getModelObjectsMap().put(petriNetObject.getId(), dataDto);
        }

        return roomDto;
    }

    @Override
    public RoomDto createNewRoom(String urlPassword, boolean isFormBuilderRoom) {
        if (isFormBuilderRoom) {
            return new RoomDto(urlPassword, true);
        }

        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setUrlPassword(urlPassword);
        try {
            roomEntity.setData(CloneUtil.serialize(new PetriNet()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        roomRepository.save(roomEntity);

        return new RoomDto(urlPassword);
    }

    @Override
    public void updateRoom(RoomDto roomDto) {
        if (roomDto.isFormBuilderRoom()) {
            return;
        }

        RoomEntity roomEntity = roomRepository.getByUrlPassword(roomDto.getPassword());
        Collection<DataDto> values = roomDto.getModelObjectsMap().values();
        PetriNet petriNet = new PetriNet();
        for (DataDto dataDto : values) {
            PetriNetObject petriNetObject = new PetriNetObject();
            petriNetObject.setId(dataDto.getObjectID());
            petriNetObject.setObjectType(dataDto.getObjectType());
            petriNetObject.setObject(dataDto.getObject());

            petriNet.getPetriNetObjects().add(petriNetObject);
        }
        try {
            roomEntity.setData(CloneUtil.serialize(petriNet));
        } catch (Exception e) {
            e.printStackTrace();
        }

        roomRepository.save(roomEntity);
    }
}
