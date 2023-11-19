package com.cts.iiht.memberservice;

import com.cts.iiht.memberservice.controller.MemberServiceController;
import com.cts.iiht.memberservice.entity.ProjectMember;
import com.cts.iiht.memberservice.exception.DataValidationException;
import com.cts.iiht.memberservice.model.APIResponse;
import com.cts.iiht.memberservice.model.AddMemberCommand;
import com.cts.iiht.memberservice.model.ProjectMemberDto;
import com.cts.iiht.memberservice.model.TeamMembers;
import com.cts.iiht.memberservice.service.AddMemberCommandHandler;
import com.cts.iiht.memberservice.service.QueryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @InjectMocks
    MemberServiceController memberServiceController;

    @Mock
    private AddMemberCommandHandler addMemberCommandHandler;

    @Mock
    private QueryService queryService;


    @Test
    public void test_addMemberDetail() throws Exception{


        AddMemberCommand addMemberCommand = new AddMemberCommand();
        addMemberCommand.setMemberId("123455");
        addMemberCommand.setMemberName("Anoop");
        addMemberCommand.setAllocationPercentage(100);
        addMemberCommand.setProjectStartDate(LocalDate.of(2023,02,22));
        addMemberCommand.setProjectEndDate(LocalDate.of(2023,05,22));
        ResponseEntity<APIResponse> responseEntity =  memberServiceController.addProjectMember(addMemberCommand);
        System.out.println("API Response "+responseEntity.getStatusCode().toString());
       Assertions.assertEquals("200 OK", responseEntity.getStatusCode().toString());
    }


    @Test
    void test_GetAllMember() throws Exception {

        TeamMembers teamMembers = new TeamMembers();
        ProjectMemberDto dto = new ProjectMemberDto();
        dto.setMemberId("123456");
        dto.setMemberName("Anoop");
        dto.setYearsOfExperienc(9);
        dto.setAllocationPercentage(100);
        ProjectMemberDto dto1 = new ProjectMemberDto();
        dto1.setMemberId("123459");
        dto1.setMemberName("Anjali");
        dto1.setYearsOfExperienc(7);
        dto1.setAllocationPercentage(100);

        List<ProjectMemberDto> list = new ArrayList<>();
        list.add(dto);
        list.add(dto1);
        teamMembers.setTeamMembers(list);

        Mockito.when(queryService.getAllMembersFromProject(null,null)).thenReturn(list);
        ResponseEntity<Object> responseEntity =   memberServiceController.getAllMembers(null,null,null);
        TeamMembers teamMembers1 = (TeamMembers) responseEntity.getBody();

        System.out.println(" Team member size "+teamMembers.getTeamMembers().size());
        Assertions.assertEquals(2, teamMembers.getTeamMembers().size());
    }
}
