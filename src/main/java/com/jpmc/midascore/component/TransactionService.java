package com.jpmc.midascore.component;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    
    private final DatabaseConduit databaseConduit;
    private final IncentiveService incentiveService;

    public TransactionService(DatabaseConduit databaseConduit, IncentiveService incentiveService) {
        this.databaseConduit = databaseConduit;
        this.incentiveService = incentiveService;
    }

    public void handleTransaction(Transaction transaction) {
        if (databaseConduit.processTransaction(transaction)) {
            // Transaction processed successfully
            Incentive incentive = incentiveService.fetchIncentive(transaction);
            System.out.println("Fetched incentive: " + incentive);
            transaction.setIncentive(incentive.getAmount());
            databaseConduit.saveTransaction(transaction);
        }
    }
}
