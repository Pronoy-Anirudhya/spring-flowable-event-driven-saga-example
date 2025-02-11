package halfbloodpro.example.flowable_saga_orchestrator_engine.Service.Kafka;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component("kafkaProducerService")
public class Producer implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "saga-events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void execute(DelegateExecution execution) {
        String eventType = execution.getCurrentActivityId();
        String orderId = (String) execution.getVariable("orderId");
        String message = "{\"orderId\": \"" + orderId + "\", \"event\": \"" + eventType + "\"}";

        logger.info("************** Received a new workflow service task. Sending relevant event to Kafka. OrderId: {}, Message: {} **************", orderId, message);
        kafkaTemplate.send(TOPIC, message);
    }
}