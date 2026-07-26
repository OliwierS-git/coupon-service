package pl.java.couponService.coupon;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.java.couponService.coupon.dto.CouponCreateRequest;
import pl.java.couponService.coupon.dto.CouponResponse;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    default Coupon toEntity(CouponCreateRequest request) {
        return Coupon.create(
                request.getCode(),
                request.getCountry().name(),
                request.getMaxUsages()
        );
    }

    @Mapping(target = "usagesLeft", expression = "java(coupon.getMaxUsages() - coupon.getCurrentUsages())")
    CouponResponse toResponse(Coupon coupon);
}
