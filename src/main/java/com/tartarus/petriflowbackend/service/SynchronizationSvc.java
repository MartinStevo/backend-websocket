package com.tartarus.petriflowbackend.service;

import com.tartarus.petriflowbackend.dto.DataDto;
import com.tartarus.petriflowbackend.dto.RoomDto;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface SynchronizationSvc {

    Collection<DataDto> synchronizeOnModelerSubscribe(String roomPassword, String simpSessionId, String userName);

    Collection<DataDto> synchronizeOnFormBuilderSubscribe(String roomPassword,
                                                          String formId,
                                                          String simpSessionId,
                                                          String userName);

    void synchronizeOnDisconnect(String simpSessionId, String userName);

    RoomDto synchronizeOnModelerUpdate(String roomPassword, DataDto dataDto);

    RoomDto synchronizeOnFormBuilderUpdate(String roomPassword, String formId, DataDto dataDto);
}
