package halfbloodpro.example.flowable_saga_orchestrator_engine.Controller;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrchestratorEngineController {
    private static final Logger logger = LoggerFactory.getLogger(OrchestratorEngineController.class);

    @Autowired
    private RuntimeService runtimeService;

    @PostMapping("/start")
    public ResponseEntity<String> startOrderSaga(@RequestParam String orderId) {
        logger.info("Starting Order Saga for orderId = {}", orderId);
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", orderId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("createOrderSaga", variables);

        logger.info("Order Saga started. processInstanceId = {}", processInstance.getId());
        return ResponseEntity.ok("Order Saga Started: " + processInstance.getId());
    }
}