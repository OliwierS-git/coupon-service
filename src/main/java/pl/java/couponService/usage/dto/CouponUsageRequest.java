package pl.java.couponService.usage.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponUsageRequest {

    @NotNull(message = "userId is required")
    private UUID userId;

    public CouponUsageRequest(UUID userId, String spring2026) {
    }
}