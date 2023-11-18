package com.cts.iiht.taskservice.service;

import com.cts.iiht.taskservice.entity.Task;
import com.cts.iiht.taskservice.helper.*;
import com.cts.iiht.taskservice.model.*;
import com.cts.iiht.taskservice.repository.TaskRepository;
import lombok.NonNull;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.*;

import javax.jms.Queue;

import static com.cts.iiht.taskservice.constant.ProjectTrackerConstant.EVENT_NAME;


@Service
public class TaskCommandHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskCommandHandler.class);

    @Autowired
    private TaskServiceHelper taskServiceHelper;

    private final JmsTemplate jmsTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private Queue queue;

    public TaskCommandHandler(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
    }

    public TaskAssignedEvent sendMessage(final AssignTaskCommand assignTaskCommand) {

        TaskAssignedEvent assignedEvent = taskServiceHelper.createTaskAssignedEvent(assignTaskCommand);
        LOGGER.info("taskAssigned Event {} ",assignedEvent);

        try {
            jmsTemplate.convertAndSend(queue.getQueueName(), assignedEvent);
        } catch (Exception ex) {
            LOGGER.error("json processing error occurred {} stack trace {} ", ex.getMessage(), ex.getStackTrace());
        }
        save(assignedEvent);
        return assignedEvent;

    }


    public void save(@NonNull final TaskAssignedEvent taskAssignedEvent) {

        Task task = taskServiceHelper.createTaskAssignedEntity(taskAssignedEvent);
        taskRepository.save(task);
        LOGGER.info("Data saved successfully in Task table in command flow");

    }

}
