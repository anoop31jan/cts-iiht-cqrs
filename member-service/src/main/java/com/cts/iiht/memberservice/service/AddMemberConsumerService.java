package com.cts.iiht.memberservice.service;

import com.cts.iiht.memberservice.entity.*;
import com.cts.iiht.memberservice.helper.*;
import com.cts.iiht.memberservice.model.*;
import com.cts.iiht.memberservice.repository.*;
import lombok.*;
import org.apache.activemq.command.Message;
import org.apache.activemq.util.ByteSequence;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.*;

import static com.cts.iiht.memberservice.constant.ProjectTrackerConstant.MEMBER_CREATED_EVENT;

@Service
public class AddMemberConsumerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddMemberConsumerService.class);

    @Autowired
    MongoDbRepository mongoDbRepository;
    @Autowired
    MemberServiceHelper memberServiceHelper;



    @JmsListener(destination = "${spring.activemq.queue.name}")
    public void consume(@NonNull final Message message) {

        ByteSequence messageSequence = message.getContent();
        byte[] data = messageSequence.getData();
        MemberAddedEvent memberAddedEvent = (MemberAddedEvent) SerializationUtils.deserialize(data);
        LOGGER.info("Received messages : {} ",message);
        LOGGER.info("Received Payload : {} ",memberAddedEvent);
        LOGGER.info("event transaction id : {} ",memberAddedEvent.getTransactionId());



        if (MEMBER_CREATED_EVENT.equalsIgnoreCase(memberAddedEvent.getEventName())) {
            saveToMongoDb(memberAddedEvent);
        }

    }

    public void saveToMongoDb(@NonNull final MemberAddedEvent memberAddedEvent) {

        ProjectMemberDoc projectMember = memberServiceHelper.craeteProjectMemberEntity(memberAddedEvent);
        mongoDbRepository.save(projectMember);
        LOGGER.info("Data saved successfully to mongo db");

    }
}
