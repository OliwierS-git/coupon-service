package pl.java.couponService.usage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.java.couponService.coupon.Coupon;
import pl.java.couponService.coupon.CouponRepository;
import pl.java.couponService.coupon.common.exception.*;
import pl.java.couponService.coupon.dto.Country;
import pl.java.couponService.geo.GeoLocationClient;
import pl.java.couponService.usage.dto.CouponUsageRequest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponUsageServiceTest {

    @Mock
    private CouponRepository couponRepository;
    @Mock
    private GeoLocationClient geoLocationClient;
    @Mock
    private CouponUsageRegistrar couponUsageRegistrar;

    @InjectMocks
    private CouponUsageService couponUsageService;

    private final UUID userId = UUID.randomUUID();

    @Test
    void happyPath_usesCouponSuccessfully() {
        var request = new CouponUsageRequest(userId);
        Coupon coupon = Coupon.create("spring2026", "PL", 100);
        CouponUsage savedUsage = CouponUsage.create(coupon, userId, Country.PL);

        when(couponRepository.findByCode("SPRING2026")).thenReturn(Optional.of(coupon));
        when(geoLocationClient.resolveCountry("8.8.8.8")).thenReturn(Country.PL);
        when(couponUsageRegistrar.register(request, "SPRING2026", coupon, Country.PL))
                .thenReturn(savedUsage);

        var response = couponUsageService.useCoupon("spring2026", request, "8.8.8.8");

        assertThat(response.couponCode()).isEqualTo("SPRING2026");
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.usagesLeftAfter()).isEqualTo(99);
    }

    @Test
    void badPath_couponNotFound_throwsCouponNotFoundException() {
        var request = new CouponUsageRequest(userId);
        when(couponRepository.findByCode("NIEISTNIEJE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponUsageService.useCoupon("NIEISTNIEJE", request, "8.8.8.8"))
                .isInstanceOf(CouponNotFoundException.class);

        verifyNoInteractions(geoLocationClient);
        verifyNoInteractions(couponUsageRegistrar);
    }

    @Test
    void badPath_wrongCountry_throwsCountryNotAllowedException() {
        var request = new CouponUsageRequest(userId);
        Coupon coupon = Coupon.create("SPRING2026", "PL", 100);

        when(couponRepository.findByCode("SPRING2026")).thenReturn(Optional.of(coupon));
        when(geoLocationClient.resolveCountry("8.8.8.8")).thenReturn(Country.DE);

        assertThatThrownBy(() -> couponUsageService.useCoupon("SPRING2026", request, "8.8.8.8"))
                .isInstanceOf(CountryNotAllowedException.class);

        verifyNoInteractions(couponUsageRegistrar);
    }

    @Test
    void badPath_registrarRejects_propagatesException() {
        var request = new CouponUsageRequest(userId);
        Coupon coupon = Coupon.create("MAXED", "PL", 1);

        when(couponRepository.findByCode("MAXED")).thenReturn(Optional.of(coupon));
        when(geoLocationClient.resolveCountry("8.8.8.8")).thenReturn(Country.PL);
        when(couponUsageRegistrar.register(request, "MAXED", coupon, Country.PL))
                .thenThrow(new CouponExhaustedException("Coupon MAXED has no usages left"));

        assertThatThrownBy(() -> couponUsageService.useCoupon("MAXED", request, "8.8.8.8"))
                .isInstanceOf(CouponExhaustedException.class);
    }
}