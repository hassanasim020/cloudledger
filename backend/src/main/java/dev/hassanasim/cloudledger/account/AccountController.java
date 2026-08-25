package dev.hassanasim.cloudledger.account;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountRepository accounts;
    public AccountController(AccountRepository accounts) { this.accounts = accounts; }

    @GetMapping
    public List<Account> all() {
        return accounts.findAll(Sort.by("createdAt").descending());
    }
}

