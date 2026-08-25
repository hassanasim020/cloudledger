package dev.hassanasim.cloudledger.transaction;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ledger_transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LedgerTransaction {
    @Id private UUID id;
    @Column(nullable = false) private UUID sourceAccountId;
    @Column(nullable = false) private UUID destinationAccountId;
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal amount;
    @Column(nullable = false, length = 3) private String currency;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TransactionStatus status;
    @Column(nullable = false, unique = true) private String reference;
    @Column(nullable = false) private Instant createdAt;
}

