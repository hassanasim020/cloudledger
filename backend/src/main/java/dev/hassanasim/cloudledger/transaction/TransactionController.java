package dev.hassanasim.cloudledger.transaction;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionRepository transactions;
    private final TransferService transferService;
    public TransactionController(TransactionRepository transactions, TransferService transferService) {
        this.transactions = transactions; this.transferService = transferService;
    }
    @GetMapping public List<LedgerTransaction> all() {
        return transactions.findAll(Sort.by("createdAt").descending());
    }
    @PostMapping("/transfer") @ResponseStatus(HttpStatus.CREATED)
    public LedgerTransaction transfer(@Valid @RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }
}

