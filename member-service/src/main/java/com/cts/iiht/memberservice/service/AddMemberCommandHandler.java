package com.cts.iiht.memberservice.service;

import com.cts.iiht.memberservice.entity.ProjectMember;
import com.cts.iiht.memberservice.helper.*;
import com.cts.iiht.memberservice.model.*;
import com.cts.iiht.memberservice.repository.MemberRepository;
import lombok.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.*;

import javax.jms.JMSException;
import javax.jms.Queue;
import java.time.LocalDate;

@Service
public class AddMemberCommandHandler {

    private static final Logger LOGGER =LoggerFactory.getLogger(AddMemberCommandHandler.class);

    private final JmsTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private MemberServiceHelper memberServiceHelper;

    public AddMemberCommandHandler(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
    }

    public MemberAddedEvent sendMessage(@NonNull final AddMemberCommand addMemberCommand) {
        MemberAddedEvent event =memberServiceHelper.createMemberAddedEvent(addMemberCommand);

        LOGGER.info("MemberAddedEvent {} ",event);
        //create message

        try {
            jmsTemplate.convertAndSend(queue.getQueueName(), event);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        save(event);
        return event;
    }


    public void save(@NonNull final MemberAddedEvent memberAddedEvent) {

        ProjectMember projectMember = memberServiceHelper.craeteProjectMemberEntityForMySQL(memberAddedEvent);
        memberRepository.save(projectMember);
        LOGGER.info("Data saved successfully");

    }

    public ProjectMember getProjectMemberByMemberId(final String memberId){

        return  memberRepository.getProjectMemberBymemberId(memberId);

    }



    public void updateMemberAllocationpercentage(@NonNull final ProjectMember projectMember) {

        if (projectMember.getProjectEndDate().isBefore(LocalDate.now())) {
            projectMember.setAllocationPercentage(0);

        } else{
            projectMember.setAllocationPercentage(100);
        }
        memberRepository.save(projectMember);
    }


}
