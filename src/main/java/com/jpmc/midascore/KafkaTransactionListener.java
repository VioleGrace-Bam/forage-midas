package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
// This class listens to Kafka messages on the specified topic and processes transactions
public class KafkaTransactionListener {
    
    @KafkaListener(
        topics = "${general.kafka-topic}", 
        groupId = "midas-core-group")
    
    
        public void listen(Transaction transaction) {
        // Process the transaction
        System.out.println("Received transaction: " + transaction);
    }
}
