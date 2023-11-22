package com.cts.iiht.taskservice;

import com.cts.iiht.taskservice.controller.TaskController;
import com.cts.iiht.taskservice.external.MemberService;
import com.cts.iiht.taskservice.external.client.ProjectMemberClient;
import com.cts.iiht.taskservice.model.*;
import com.cts.iiht.taskservice.service.QueryService;
import com.cts.iiht.taskservice.service.TaskCommandHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @InjectMocks
    TaskController taskController;

    @Mock
    private MemberService memberService;

    @Mock
    private TaskCommandHandler taskCommandHandler;

    @Mock
    QueryService queryService;


    @Test
    public void test_assignTask() throws Exception{

        TaskAssignedEvent taskAssignedEvent = new TaskAssignedEvent();
        taskAssignedEvent.setMemberId("65807");
        ProjectMemberClient projectMemberClient = new ProjectMemberClient();
        projectMemberClient.setMemberId("65807");
        projectMemberClient.setProjectEndDate(LocalDate.of(2023,12,31));

        Mockito.when(memberService.getMemberDetails(Mockito.anyString())).thenReturn(projectMemberClient);
        Mockito.when(taskCommandHandler.sendMessage(Mockito.any())).thenReturn(taskAssignedEvent);

        AssignTaskCommand assignTaskCommand = new AssignTaskCommand();
        assignTaskCommand.setMemberId("65807");
        assignTaskCommand.setDeliverables("login page");
        assignTaskCommand.setTaskName("userstory-12345");
        assignTaskCommand.setTaskEndDate(LocalDate.of(2023,12,31));
        assignTaskCommand.setTaskStartDate(LocalDate.of(2023,03,22));
        ResponseEntity<Object> response = taskController.assignTask(assignTaskCommand);
        APIResponse apiResponse = (APIResponse) response.getBody();
        TaskAssignedEvent event = (TaskAssignedEvent) apiResponse.getData();

        System.out.println(" Result : "+event.getMemberId());

        Assertions.assertEquals("65807", event.getMemberId());



    }

    @Test
    public void test_getTaskListFormember(){

        TaskDetailsDto taskDetailsDto = new TaskDetailsDto();
        taskDetailsDto.setMemberId("123456");
        taskDetailsDto.setDeliverables("registration page");
        taskDetailsDto.setTaskName("user-story-1234");
        taskDetailsDto.setProjectEndDate(LocalDate.of(2023,12,31));
        taskDetailsDto.setProjectStartDate(LocalDate.of(2023,07,07));

        List<TaskDetailsDto> taskList = new ArrayList<>();
        taskList.add(taskDetailsDto);

        Mockito.when(queryService.getListOfTaskDetails("123456")).thenReturn(taskList);

        ResponseEntity<Object> response = taskController.getTaskListForMember("123456");

       Tasks tasks = (Tasks) response.getBody();
       Assertions.assertEquals(1, tasks.getTasks().size());

    }
}
