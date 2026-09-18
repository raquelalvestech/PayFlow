


package com.payflow.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDTO (
        BigDecimal value,
        UUID payerId,
        UUID payeeId
) {}

