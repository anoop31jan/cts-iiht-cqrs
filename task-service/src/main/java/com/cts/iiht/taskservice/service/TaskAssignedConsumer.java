package com.cts.iiht.taskservice.service;

import com.cts.iiht.taskservice.entity.*;
import com.cts.iiht.taskservice.helper.*;
import com.cts.iiht.taskservice.model.*;
import lombok.*;
import org.apache.activemq.command.Message;
import org.apache.activemq.util.ByteSequence;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.*;

import static com.cts.iiht.taskservice.constant.ProjectTrackerConstant.TASK_ASSIGNED_EVENT;


@Service
public class TaskAssignedConsumer {
    @Autowired
    MongoRepository mongoRepository;

    @Autowired
    TaskServiceHelper taskServiceHelper;

    private static Logger LOGGER = LoggerFactory.getLogger(TaskAssignedConsumer.class);

    @JmsListener(destination = "${spring.activemq.queue.name}")
    public void consume(@NonNull final Message message) {

        ByteSequence messageSequence = message.getContent();
        byte[] data = messageSequence.getData();
        TaskAssignedEvent taskAssignedEvent = (TaskAssignedEvent) SerializationUtils.deserialize(data);
        LOGGER.info("Received messages : {} ",message);
        LOGGER.info("Received Payload : {} ",taskAssignedEvent);
        LOGGER.info("event transaction id : {} ",taskAssignedEvent.getTransactionId());
        LOGGER.info("Data saved successfully in Task table");
        LOGGER.info("Data saved successfully");


        if (TASK_ASSIGNED_EVENT.equalsIgnoreCase(taskAssignedEvent.getEventName())) {
            save(taskAssignedEvent);
        }

    }

    public void save(@NonNull final TaskAssignedEvent taskAssignedEvent) {

        TaskDoc task = taskServiceHelper.createTaskAssignedEntityForReadFlow(taskAssignedEvent);
        mongoRepository.save(task);
        LOGGER.info("Data saved successfully in Task table in mongo db");

    }

}
