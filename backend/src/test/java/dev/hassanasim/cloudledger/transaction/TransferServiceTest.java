package dev.hassanasim.cloudledger.transaction;

import dev.hassanasim.cloudledger.account.*;
import dev.hassanasim.cloudledger.common.BusinessException;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceTest {
    private final AccountRepository accounts = mock(AccountRepository.class);
    private final TransactionRepository transactions = mock(TransactionRepository.class);
    private final TransferService service = new TransferService(accounts, transactions);

    @Test void transfersFundsAtomically() {
        Account source = account("100.00"); Account destination = account("20.00");
        when(accounts.findById(source.getId())).thenReturn(Optional.of(source));
        when(accounts.findById(destination.getId())).thenReturn(Optional.of(destination));
        when(transactions.save(any())).thenAnswer(i -> i.getArgument(0));
        LedgerTransaction result = service.transfer(new TransferRequest(source.getId(), destination.getId(), new BigDecimal("25.00"), "PKR"));
        assertEquals(new BigDecimal("75.00"), source.getBalance());
        assertEquals(new BigDecimal("45.00"), destination.getBalance());
        assertEquals(TransactionStatus.COMPLETED, result.getStatus());
    }

    @Test void rejectsInsufficientFunds() {
        Account source = account("10.00"); Account destination = account("20.00");
        when(accounts.findById(source.getId())).thenReturn(Optional.of(source));
        when(accounts.findById(destination.getId())).thenReturn(Optional.of(destination));
        assertThrows(BusinessException.class, () -> service.transfer(
            new TransferRequest(source.getId(), destination.getId(), new BigDecimal("25.00"), "PKR")));
    }

    private Account account(String balance) {
        return Account.builder().id(UUID.randomUUID()).ownerName("Demo").accountNumber(UUID.randomUUID().toString())
            .balance(new BigDecimal(balance)).currency("PKR").status(AccountStatus.ACTIVE).createdAt(Instant.now()).build();
    }
}
