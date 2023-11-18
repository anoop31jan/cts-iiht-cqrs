package com.cts.iiht.memberservice.service;

import com.cts.iiht.memberservice.entity.*;
import com.cts.iiht.memberservice.model.ProjectMemberDto;
import com.cts.iiht.memberservice.repository.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import javax.servlet.http.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

@Service
public class QueryService {
    @Autowired
    MongoDbRepository mongoDbRepository;

    public List<ProjectMemberDto> getAllMembersFromProject(@NonNull final Pageable pageable,
                                                           @NonNull final HttpServletResponse response){
       final Page<ProjectMemberDoc> membersOnPage = mongoDbRepository.findAll(pageable);
       final List<ProjectMemberDoc> members = membersOnPage.getContent();
       List<ProjectMemberDto>  listOfProjectMembersDto = new ArrayList<>();
       if (!CollectionUtils.isEmpty(members)) {
           listOfProjectMembersDto = members.stream()
                   .map(member -> new ProjectMemberDto(member.getMemberName(),
                           member.getMemberId(),
                           member.getYearsOfExperience(),
                           member.getSkillset(),
                           member.getDescription(),
                           member.getProjectStartDate(),
                           member.getProjectEndDate(),
                           member.getAllocationPercentage())).collect(Collectors.toList());
           listOfProjectMembersDto.sort(Comparator.comparing(ProjectMemberDto::getYearsOfExperienc).reversed());

       }
        if (membersOnPage.hasNext()){
            Pageable nextPageable1 = pageable.next();
            String nextPageUrl = "/members?page="+ nextPageable1.getPageNumber() + "&size="+nextPageable1.getPageSize();
            response.setHeader(HttpHeaders.LINK, "<"+ nextPageUrl + ">; rel=\"next\"");
        }
        return listOfProjectMembersDto;
    }

    public ProjectMemberDoc getProjectMemberByMemberId(final String memberId){

        return  mongoDbRepository.findByMemberId(memberId);

    }
}

