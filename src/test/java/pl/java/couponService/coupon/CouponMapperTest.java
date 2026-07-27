package pl.java.couponService.coupon;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.java.couponService.coupon.dto.Country;
import pl.java.couponService.coupon.dto.CouponCreateRequest;
import pl.java.couponService.coupon.dto.CouponResponse;

import static org.assertj.core.api.Assertions.assertThat;

class CouponMapperTest {

    private final CouponMapper mapper = Mappers.getMapper(CouponMapper.class);

    @Test
    void happyPath_toEntity_normalizesCodeAndMapsFields() {
        var request = new CouponCreateRequest("spring2026", Country.PL, 50);

        Coupon result = mapper.toEntity(request);

        assertThat(result.getCode()).isEqualTo("SPRING2026");
        assertThat(result.getCountry()).isEqualTo(Country.PL);
        assertThat(result.getMaxUsages()).isEqualTo(50);
        assertThat(result.getCurrentUsages()).isZero();
    }

    @Test
    void happyPath_toResponse_calculatesUsagesLeft() {
        Coupon coupon = Coupon.create("SUMMER26", "DE", 10);
        for (int i = 0; i < 3; i++) coupon.incrementUsage();

        CouponResponse response = mapper.toResponse(coupon);

        assertThat(response.usagesLeft()).isEqualTo(7);
    }

    @Test
    void badPath_toEntity_invalidCountryCode_throws() {
        // To nie jest bad path.
        // Request używa poprawnego enumu Country.PL i niczego nie rzuca.
        // Taki test daje fałszywe poczucie pokrycia scenariusza błędnego.
        // Propozycja: albo usunac ten test, albo zastapic go testem walidacji requestu
        // (np. blank code, null maxUsages) na poziomie kontrolera.
        var request = new CouponCreateRequest("CODE123", Country.PL, 10);

        Coupon result = mapper.toEntity(request);

        assertThat(result.getCountry()).isNotNull();
    }
}
