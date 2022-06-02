package com.tartarus.petriflowbackend.service;

import com.tartarus.petriflowbackend.config.RoomsHolder;
import com.tartarus.petriflowbackend.dto.DataDto;
import com.tartarus.petriflowbackend.dto.RoomDto;
import com.tartarus.petriflowbackend.enu.ObjectTypeEnu;
import com.tartarus.petriflowbackend.enu.OperationTypeEnu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

@Service
public class SynchronizationService implements SynchronizationSvc {

    private static final Logger logger = LoggerFactory.getLogger(SynchronizationService.class);

    private final RoomsHolder roomsHolder;

    private final RoomSvc roomSvc;

    @Autowired
    public SynchronizationService(RoomsHolder roomsHolder, RoomSvc roomSvc) {
        this.roomsHolder = roomsHolder;
        this.roomSvc = roomSvc;
    }

    @Override
    public Collection<DataDto> synchronizeOnModelerSubscribe(String roomPassword, String simpSessionId, String userName) {
        return synchronizeOnSubscribe(roomPassword, simpSessionId, userName, false);
    }

    @Override
    public Collection<DataDto> synchronizeOnFormBuilderSubscribe(String roomPassword, String formId, String simpSessionId, String userName) {
        return synchronizeOnSubscribe(
                constructFormBuilderPassword(roomPassword, formId),
                simpSessionId,
                userName,
                true
        );
    }

    private String constructFormBuilderPassword(String roomPassword, String formId) {
        return roomPassword + "#" + formId;
    }

    private Collection<DataDto> synchronizeOnSubscribe(String roomPassword,
                                                       String simpSessionId,
                                                       String userName,
                                                       boolean isFormBuilderRoom) {
        if (roomsHolder.containsRoom(roomPassword)) {
            RoomDto roomDto = roomsHolder.signUserIntoRoom(userName, roomPassword, simpSessionId);

            logger.info(String.format("Room is active, sending actual data. User=%s, room=%s", userName, roomPassword));

            return processDataForInitSend(roomDto.getModelObjectsMap().values());
        } else {
            if (roomSvc.existsByUrlPassword(roomPassword)) {
                RoomDto roomDto = roomSvc.getByUrlPassword(roomPassword);
                roomsHolder.addRoom(roomDto);
                roomsHolder.signUserIntoRoom(userName, roomPassword, simpSessionId);

                logger.info(String.format("Room exists in database. First interaction in this room, sending " +
                        "data from database and adding it to memory. User=%s, room=%s", userName, roomPassword));

                return processDataForInitSend(roomDto.getModelObjectsMap().values());
            } else {
                RoomDto roomDto = roomSvc.createNewRoom(roomPassword, isFormBuilderRoom);
                roomsHolder.addRoom(roomDto);
                roomsHolder.signUserIntoRoom(userName, roomPassword, simpSessionId);

                logger.info(String.format("Room does not exist. Creating new and adding it to memory. " +
                        "User=%s, room=%s", userName, roomPassword));

                return Collections.singletonList(processDataForInitSend(new DataDto()));
            }
        }
    }

    @Override
    public void synchronizeOnDisconnect(String simpSessionId, String userName) {
        RoomDto roomDto = roomsHolder.removeUserFromRoomBySimpSessionId(userName, simpSessionId);
        logger.info(String.format("Removed user %s from room %s", userName, roomDto.getPassword()));

        if (roomDto.getUsers().isEmpty()) {
            roomSvc.updateRoom(roomDto);
            roomsHolder.removeRoom(roomDto);

            String format = roomDto.isFormBuilderRoom() ?
                    "Room %s is inactive and removed from memory." :
                    "Room %s is inactive, saved in database and removed from memory.";
            logger.info(String.format(format, roomDto.getPassword()));
        }
    }

    @Override
    public RoomDto synchronizeOnModelerUpdate(String roomPassword, DataDto dataDto) {
        return synchronizeOnUpdate(roomPassword, dataDto);
    }

    //TODO not needed to save state in database because form builder is saved after clicking button
    @Override
    public RoomDto synchronizeOnFormBuilderUpdate(String roomPassword, String formId, DataDto dataDto) {
        return synchronizeOnUpdate(constructFormBuilderPassword(roomPassword, formId), dataDto);
    }

    private RoomDto synchronizeOnUpdate(String roomPassword, DataDto dataDto) {
        logger.info(String.format("Room=%s, data=%s", roomPassword, dataDto));
        RoomDto roomDto = roomsHolder.getRoom(roomPassword);

        if (OperationTypeEnu.CREATE.equals(dataDto.getOperationType())) {
            roomDto.getModelObjectsMap().put(dataDto.getObjectID(), dataDto);
            logger.info(String.format(
                    "Created new object of type=%s with id=%s", dataDto.getObjectType(), dataDto.getObjectID()));
        }
        if (OperationTypeEnu.UPDATE.equals(dataDto.getOperationType())) {
            roomDto.getModelObjectsMap().put(dataDto.getObjectID(), dataDto);
            logger.info(String.format(
                    "Updated object of type=%s with id=%s", dataDto.getObjectType(), dataDto.getObjectID()));
        }
        if (OperationTypeEnu.DELETE.equals(dataDto.getOperationType())) {
            if (ObjectTypeEnu.MODEL.equals(dataDto.getObjectType())) {
                roomDto.setModelObjectsMap(new LinkedHashMap<>());
                logger.info("Deleted whole model");
            } else {
                roomDto.getModelObjectsMap().remove(dataDto.getObjectID());
                logger.info(String.format(
                        "Deleted object of type=%s with id=%s", dataDto.getObjectType(), dataDto.getObjectID()));
            }
        }

        return roomDto;
    }

    private DataDto processDataForInitSend(DataDto dataDto) {
        dataDto.setOperationType(OperationTypeEnu.INIT);
        return dataDto;
    }

    private Collection<DataDto> processDataForInitSend(Collection<DataDto> dataDtoCollection) {
        dataDtoCollection.forEach(this::processDataForInitSend);
        return dataDtoCollection;
    }
}
