package dev.hassanasim.cloudledger.transaction;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;
public record TransferRequest(
    @NotNull UUID sourceAccountId,
    @NotNull UUID destinationAccountId,
    @NotNull @DecimalMin("0.01") BigDecimal amount,
    @NotBlank @Pattern(regexp = "[A-Z]{3}") String currency
) {}

