package pl.java.couponService.usage;

import org.mapstruct.Mapper;
import pl.java.couponService.coupon.Coupon;
import pl.java.couponService.coupon.dto.Country;
import pl.java.couponService.usage.dto.CouponUsageRequest;
import pl.java.couponService.usage.dto.CouponUsageResponse;

@Mapper(componentModel = "spring")
public interface CouponUsageMapper {

    default CouponUsage toEntity(CouponUsageRequest request, Coupon coupon, Country resolvedCountry) {
        return CouponUsage.create(
                coupon,
                request.getUserId(),
                resolvedCountry
        );
    }
    default CouponUsageResponse toResponse(CouponUsage usage, int usagesLeftAfter) {
        return CouponUsageResponse.from(usage, usagesLeftAfter);
    }
}