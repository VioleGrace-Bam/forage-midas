package com.jpmc.midascore;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
// This class listens to Kafka messages on the specified topic and processes transactions
public class KafkaTransactionListener {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @KafkaListener(
        topics = "${general.kafka-topic}", 
        groupId = "midas-core-group")
    
    
        public void listen(Transaction transaction) {

        Optional<UserRecord> senderOpt = userRepository.findById(transaction.getSenderId());
        Optional<UserRecord> recipientOpt = userRepository.findById(transaction.getRecipientId());
        
        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            System.out.println("Sender or recipient not found for transaction: " + transaction);
            return;
        }

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        if (sender.getBalance() < transaction.getAmount()) {
            System.out.println("Insufficient balance for sender: " + sender.getId() + " for transaction: " + transaction);
            return;
        }

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());
        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount());
        transactionRecordRepository.save(record);

        System.out.println("Transaction processed successfully: " + transaction);

        for (UserRecord user : userRepository.findAll()) {
            System.out.println(user.getName() + ": " + user.getBalance());
        }


        // Process the transaction
        System.out.println("Received transaction: " + transaction);
    }
}
