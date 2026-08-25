package dev.hassanasim.cloudledger.transaction;

import dev.hassanasim.cloudledger.account.*;
import dev.hassanasim.cloudledger.common.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class TransferService {
    private final AccountRepository accounts;
    private final TransactionRepository transactions;
    public TransferService(AccountRepository accounts, TransactionRepository transactions) {
        this.accounts = accounts;
        this.transactions = transactions;
    }

    @Transactional
    public LedgerTransaction transfer(TransferRequest request) {
        if (request.sourceAccountId().equals(request.destinationAccountId()))
            throw new BusinessException("Source and destination accounts must differ");
        Account source = accounts.findById(request.sourceAccountId())
            .orElseThrow(() -> new BusinessException("Source account not found"));
        Account destination = accounts.findById(request.destinationAccountId())
            .orElseThrow(() -> new BusinessException("Destination account not found"));
        if (source.getStatus() != AccountStatus.ACTIVE || destination.getStatus() != AccountStatus.ACTIVE)
            throw new BusinessException("Both accounts must be active");
        if (!source.getCurrency().equals(request.currency()) || !destination.getCurrency().equals(request.currency()))
            throw new BusinessException("Currency must match both accounts");
        if (source.getBalance().compareTo(request.amount()) < 0)
            throw new BusinessException("Insufficient funds");

        source.setBalance(source.getBalance().subtract(request.amount()));
        destination.setBalance(destination.getBalance().add(request.amount()));
        return transactions.save(LedgerTransaction.builder()
            .id(UUID.randomUUID()).sourceAccountId(source.getId()).destinationAccountId(destination.getId())
            .amount(request.amount()).currency(request.currency()).status(TransactionStatus.COMPLETED)
            .reference("CL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
            .createdAt(Instant.now()).build());
    }
}

