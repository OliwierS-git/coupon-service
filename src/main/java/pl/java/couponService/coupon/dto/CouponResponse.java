package pl.java.couponService.coupon.dto;

import lombok.Builder;
import pl.java.couponService.coupon.Coupon;

import java.time.Instant;
import java.util.UUID;
@Builder
public record CouponResponse(UUID id, String code, Country country, int maxUsages, int currentUsages, int usagesLeft,
                             Instant createdAt) {

    public static CouponResponse from(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .country(coupon.getCountry())
                .maxUsages(coupon.getMaxUsages())
                .currentUsages(coupon.getCurrentUsages())
                .usagesLeft(coupon.getMaxUsages() - coupon.getCurrentUsages())
                .createdAt(coupon.getCreatedAt())
                .build();
    }
}