package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {

    private final TransactionRecordRepository transactionRecordRepository;
    private final UserRepository userRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public void saveTransaction(Transaction transaction) {

        UserRecord sender = queryUser(transaction.getSenderId());
        UserRecord recipient = queryUser(transaction.getRecipientId());

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + transaction.getIncentive());

        TransactionRecord transactionRecord = new TransactionRecord(
                sender, recipient, transaction.getAmount(), transaction.getIncentive());
        transactionRecordRepository.save(transactionRecord);

        save(sender);
        save(recipient);
        System.out.println("Transaction processed successfully: " + transaction);
        
        for (UserRecord user : userRepository.findAll()) {
            System.out.println(user.getName() + ": " + user.getBalance());
        }
    }

    public UserRecord queryUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public boolean processTransaction(Transaction transaction) {
        UserRecord sender = queryUser(transaction.getSenderId());
        UserRecord recipient = queryUser(transaction.getRecipientId());

        if (sender == null || recipient == null) {
            System.out.println("Sender or recipient not found for transaction: " + transaction);
            return false;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            System.out.println("Insufficient balance for sender: " + sender.getId() + " for transaction: " + transaction);
            return false;
        }

        return true;
    }

    public float queryBalance(Long userId) {
        UserRecord userRecord = queryUser(userId);
        if (userRecord == null) {
            return 0;
        }
        return userRecord.getBalance();
    }

}
