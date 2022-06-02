package com.tartarus.petriflowbackend.controller;

import com.tartarus.petriflowbackend.dto.DataDto;
import com.tartarus.petriflowbackend.dto.RoomDto;
import com.tartarus.petriflowbackend.enu.RoomTypeEnu;
import com.tartarus.petriflowbackend.service.SynchronizationSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import javax.transaction.NotSupportedException;
import java.security.Principal;
import java.util.Collection;

@Controller
@CrossOrigin(origins = "*") //todo check if this is needed
public class SynchronizationController implements ApplicationListener<AbstractSubProtocolEvent> {

    private static final Logger logger = LoggerFactory.getLogger(SynchronizationController.class);

    private final SimpMessagingTemplate messagingTemplate;

    private final SynchronizationSvc synchronizationSvc;

    @Autowired
    public SynchronizationController(SimpMessagingTemplate messagingTemplate, SynchronizationSvc synchronizationSvc) {
        this.messagingTemplate = messagingTemplate;
        this.synchronizationSvc = synchronizationSvc;
    }

    @Override
    public void onApplicationEvent(AbstractSubProtocolEvent event) {
        String userName = event.getUser().getName();

        if (event instanceof SessionSubscribeEvent) {
            String simpDestination = (String) event.getMessage().getHeaders().get("simpDestination");
            simpDestination = simpDestination == null ? "" : simpDestination.replaceAll("/user", "");

            String[] urlPaths = simpDestination.split("/");
            String roomPassword = urlPaths[3];
            String roomType = urlPaths[4]; // /modeler or /formbuilder
            String simpSessionId = (String) event.getMessage().getHeaders().get("simpSessionId");

            if (RoomTypeEnu.MODELER.getCode().equals(roomType)) {
                synchronizeOnModelerSubscribe(userName, simpDestination, roomPassword, simpSessionId);

            } else if (RoomTypeEnu.FORM_BUILDER.getCode().equals(roomType)) {
                String formId = urlPaths[5];
                synchronizeOnFormBuilderSubscribe(userName, simpDestination, roomPassword, formId, simpSessionId);
            } else {
                throw new RuntimeException(
                        new NotSupportedException(String.format("Room type %s is not supported.", roomType))
                );
            }

        } else if (event instanceof SessionDisconnectEvent) {
            String simpSessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
            logger.info("Disconnecting simpSessionId=" + simpSessionId);

            synchronized (this) {
                synchronizationSvc.synchronizeOnDisconnect(simpSessionId, userName);
            }
        }
    }

    private synchronized void synchronizeOnModelerSubscribe(String userName,
                                                            String simpDestination,
                                                            String roomPassword,
                                                            String simpSessionId) {
        Collection<DataDto> dataDtoCollection = synchronizationSvc.synchronizeOnModelerSubscribe(
                roomPassword, simpSessionId, userName
        );

        for (DataDto dataDto : dataDtoCollection) {
            logger.info("Sending data, objectID=" + dataDto);
            messagingTemplate.convertAndSendToUser(userName, simpDestination, dataDto);
        }
    }

    private synchronized void synchronizeOnFormBuilderSubscribe(String userName,
                                                                String simpDestination,
                                                                String roomPassword,
                                                                String formId,
                                                                String simpSessionId) {
        Collection<DataDto> dataDtoCollection = synchronizationSvc.synchronizeOnFormBuilderSubscribe(
                roomPassword, formId, simpSessionId, userName
        );

        for (DataDto dataDto : dataDtoCollection) {
            logger.info("Sending data, objectID=" + dataDto);
            messagingTemplate.convertAndSendToUser(userName, simpDestination, dataDto);
        }
    }

    @MessageMapping("/room/{roomPassword}/modeler")
    public void onModelerChange(@DestinationVariable String roomPassword,
                                @Header("simpSessionId") String simpSessionId, //this is just for testing
                                DataDto dataDto,
                                Principal principal) throws Exception {
        logger.info(String.format(
                "Incoming data, user=%s, room=%s, objectID=%s", principal.getName(), roomPassword, dataDto.getObjectID()
        ));

        RoomDto roomDto = synchronizationSvc.synchronizeOnModelerUpdate(roomPassword, dataDto);

        for (String user : roomDto.getUsers()) {
            if (user.equals(principal.getName())) {
                continue;
            }
            logger.info(String.format("Sending to user %s in room %s", user, roomPassword));

            String returnUrl = String.format("/queue/room/%s/modeler", roomPassword);
            messagingTemplate.convertAndSendToUser(user, returnUrl, dataDto);
        }
    }

    @MessageMapping("/room/{roomPassword}/formbuilder/{formId}")
    public void onFormBuilderChange(@DestinationVariable String roomPassword,
                                    @DestinationVariable String formId,
                                    @Header("simpSessionId") String simpSessionId, //this is just for testing
                                    DataDto dataDto,
                                    Principal principal) throws Exception {
        System.out.println("TEST");

        logger.info(String.format(
                "Incoming data, user=%s, room=%s, formId=%s, objectID=%s",
                principal.getName(), roomPassword, formId, dataDto.getObjectID()
        ));

        RoomDto roomDto = synchronizationSvc.synchronizeOnFormBuilderUpdate(roomPassword, formId, dataDto);

        for (String user : roomDto.getUsers()) {
            if (user.equals(principal.getName())) {
                continue;
            }
            logger.info(String.format("Sending to user %s in room %s with formId %s", user, roomPassword, formId));

            String returnUrl = String.format("/queue/room/%s/formbuilder/%s", roomPassword, formId);
            messagingTemplate.convertAndSendToUser(user, returnUrl, dataDto);
        }
    }
}
