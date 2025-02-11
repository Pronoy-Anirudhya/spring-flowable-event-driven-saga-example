package halfbloodpro.flowable.order_service.Service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private static final String TOPIC = "saga-replies";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "saga-events", groupId = "order-group")
    public void listen(String message) {
        logger.info("************** Received message from Kafka. Message: {} **************", message);

        JSONObject json = new JSONObject(message);
        String orderId = json.getString("orderId");
        String event = json.getString("event");

        switch (event)
        {
            case "sendCreateOrder" ->
            {
                //DO relevant task e.g. update order status
                event = "OrderInitiated";
                logger.info("Order initiated. Sending message to Kafka. OrderId: {}, Event: {}", orderId, event);
                kafkaTemplate.send(TOPIC, "{\"orderId\": \"" + orderId + "\", \"event\": \"" + event + "\"}");
                break;
            }
            default ->
            {
                logger.warn("Unknown event received from Kafka. Event: {}", event);
                return;
            }
        }
    }
}