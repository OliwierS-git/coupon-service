package pl.java.couponService.usage;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.java.couponService.coupon.Coupon;
import pl.java.couponService.coupon.dto.Country;
import pl.java.couponService.usage.dto.CouponUsageRequest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CouponUsageMapperTest {

    private final CouponUsageMapper mapper = Mappers.getMapper(CouponUsageMapper.class);

    @Test
    void happyPath_toEntity_mapsAllFields() {
        Coupon coupon = Coupon.create("SPRING2026", "PL", 100);
        UUID userId = UUID.randomUUID();
        var request = new CouponUsageRequest(userId);

        CouponUsage result = mapper.toEntity(request, coupon, Country.PL);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getCoupon()).isEqualTo(coupon);
        assertThat(result.getCountryAtUsage()).isEqualTo(Country.PL);
    }

    @Test
    void badPath_nullCoupon_throwsNullPointerAtCreation() {
        var request = new CouponUsageRequest(UUID.randomUUID());

        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> mapper.toEntity(request, null, Country.PL).getCoupon().getCode()
        );
    }
}