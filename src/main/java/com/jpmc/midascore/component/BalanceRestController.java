package com.jpmc.midascore.component;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jpmc.midascore.foundation.Balance;

@RestController
public class BalanceRestController {

    private final DatabaseConduit databaseConduit;

    public BalanceRestController(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam("userId") Long userId) {
        float balance = databaseConduit.queryBalance(userId);
        return new Balance(balance);
    }
}
