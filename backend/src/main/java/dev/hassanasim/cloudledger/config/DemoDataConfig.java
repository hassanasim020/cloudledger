package dev.hassanasim.cloudledger.config;

import dev.hassanasim.cloudledger.account.*;
import dev.hassanasim.cloudledger.transaction.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Configuration
public class DemoDataConfig {
    @Bean CommandLineRunner demoData(AccountRepository accounts, TransactionRepository transactions) {
        return args -> {
            if (accounts.count() > 0) return;
            Account a = account("Horizon Trading", "PK01-CL-1001", "1250000.00");
            Account b = account("Atlas Textiles", "PK01-CL-1002", "840000.00");
            Account c = account("Nova Logistics", "PK01-CL-1003", "525000.00");
            accounts.save(a); accounts.save(b); accounts.save(c);
            transactions.save(LedgerTransaction.builder().id(UUID.randomUUID()).sourceAccountId(a.getId())
                .destinationAccountId(b.getId()).amount(new BigDecimal("75000.00")).currency("PKR")
                .status(TransactionStatus.COMPLETED).reference("CL-DEMO001").createdAt(Instant.now().minusSeconds(3600)).build());
        };
    }
    private Account account(String owner, String number, String balance) {
        return Account.builder().id(UUID.randomUUID()).ownerName(owner).accountNumber(number)
            .balance(new BigDecimal(balance)).currency("PKR").status(AccountStatus.ACTIVE).createdAt(Instant.now()).build();
    }
}

