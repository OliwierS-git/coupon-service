package pl.java.couponService.usage.dto;

import lombok.Builder;
import pl.java.couponService.usage.CouponUsage;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CouponUsageResponse(UUID usageId, String couponCode, UUID userId, Instant usedAt, int usagesLeftAfter) {

    public static CouponUsageResponse from(CouponUsage usage, int usagesLeftAfter) {
        return CouponUsageResponse.builder()
                .usageId(usage.getId())
                .couponCode(usage.getCoupon().getCode())
                .userId(usage.getUserId())
                .usedAt(usage.getUsedAt())
                .usagesLeftAfter(usagesLeftAfter)
                .build();
    }
}