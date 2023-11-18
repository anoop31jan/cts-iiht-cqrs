package com.cts.iiht.taskservice.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.*;

import java.io.Serializable;
import java.time.*;

@Data
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignedEvent extends BaseEvent implements Serializable {

    private String memberId;
    private String taskName;
    private String deliverables;
    private LocalDate taskStartDate;
    private LocalDate  taskEndDate;
}
