package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
public class KafkaTransactionListener {
    
    @KafkaListener(
        topics = "${general.kafka-topic}", 
        groupId = "midas_core_group")
    
    
        public void listen(Transaction transaction) {
        // Process the transaction
        System.out.println("Received transaction: " + transaction);
    }
}
