package com.cts.iiht.taskservice.external.client;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.*;

import java.time.*;
import java.util.*;

@Data
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberClient {
    private String memberName;
    private String memberId;
    private int yearsOfExperience;
    private List<String> skillset;
    private String description;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private int allocationPercentage;
}
