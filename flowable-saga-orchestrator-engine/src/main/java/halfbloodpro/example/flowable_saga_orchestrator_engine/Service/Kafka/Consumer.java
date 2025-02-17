package halfbloodpro.example.flowable_saga_orchestrator_engine.Service.Kafka;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
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
    
        //Gets the relevant execution Id within the workflow instance needed to correlate the event
        Execution execution = runtimeService.createExecutionQuery()
                                            .messageEventSubscriptionName(event)
                                            .singleResult();

        if (execution != null)
        {
            runtimeService.messageEventReceived(event, execution.getId());
            logger.info("Successfully correlated an event. ExecutionId: {}, OrderId: {}, Message: {}", execution.getId(), orderId, event);
        }
        else
        {
            logger.error("No workflow instance found waiting for the event. OrderId: {}, Message: {}", orderId, event);
        }
    }
}