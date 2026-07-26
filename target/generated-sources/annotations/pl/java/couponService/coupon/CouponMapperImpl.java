package pl.java.couponService.coupon;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.java.couponService.coupon.dto.CouponResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-25T22:07:49+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class CouponMapperImpl implements CouponMapper {

    @Override
    public CouponResponse toResponse(Coupon coupon) {
        if ( coupon == null ) {
            return null;
        }

        CouponResponse.CouponResponseBuilder couponResponse = CouponResponse.builder();

        couponResponse.id( coupon.getId() );
        couponResponse.code( coupon.getCode() );
        couponResponse.country( coupon.getCountry() );
        couponResponse.maxUsages( coupon.getMaxUsages() );
        couponResponse.currentUsages( coupon.getCurrentUsages() );
        couponResponse.createdAt( coupon.getCreatedAt() );

        couponResponse.usagesLeft( coupon.getMaxUsages() - coupon.getCurrentUsages() );

        return couponResponse.build();
    }
}
