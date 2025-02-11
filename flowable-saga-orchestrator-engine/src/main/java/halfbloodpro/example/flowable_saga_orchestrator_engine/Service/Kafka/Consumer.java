package halfbloodpro.example.flowable_saga_orchestrator_engine.Service.Kafka;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private RuntimeService runtimeService;

    @KafkaListener(topics = "saga-replies", groupId = "saga-group")
    public void listen(String message) {
        logger.info("************** Received message from Kafka. Message: {} **************", message);

        JSONObject json = new JSONObject(message);
        String orderId = json.getString("orderId");
        String event = json.getString("event");

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                                                        .variableValueEquals("orderId", orderId)
                                                        .singleResult();

        if (processInstance == null)
        {
            logger.warn("No active process found for orderId: {}", orderId);
            return;
        }

        logger.info("Successfully correlated an event. OrderId: {}, EventId: {}, ProcessInstanceId: {}", orderId, event, processInstance.getId());
        runtimeService.messageEventReceived(event, processInstance.getId());
    }
}