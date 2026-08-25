package dev.hassanasim.cloudledger.account;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Account {
    @Id private UUID id;
    @Column(nullable = false) private String ownerName;
    @Column(nullable = false, unique = true) private String accountNumber;
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal balance;
    @Column(nullable = false, length = 3) private String currency;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private AccountStatus status;
    @Column(nullable = false) private Instant createdAt;
}

