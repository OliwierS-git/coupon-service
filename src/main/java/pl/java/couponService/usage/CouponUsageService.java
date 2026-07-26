package pl.java.couponService.usage;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.java.couponService.coupon.Coupon;
import pl.java.couponService.coupon.CouponRepository;
import pl.java.couponService.coupon.common.exception.CountryNotAllowedException;
import pl.java.couponService.coupon.common.exception.CouponNotFoundException;
import pl.java.couponService.coupon.dto.Country;
import pl.java.couponService.geo.GeoLocationClient;
import pl.java.couponService.usage.dto.CouponUsageRequest;
import pl.java.couponService.usage.dto.CouponUsageResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponUsageService {

    private final CouponRepository couponRepository;
    private final GeoLocationClient geoLocationClient;
    private final CouponUsageRegistrar couponUsageRegistrar;

    public CouponUsageResponse useCoupon(String couponCode, CouponUsageRequest request, String requesterIp) {
        String normalizedCode = couponCode.trim().toUpperCase();

        Coupon coupon = couponRepository.findByCode(normalizedCode)
                .orElseThrow(() -> new CouponNotFoundException(normalizedCode));

        Country resolvedCountry = geoLocationClient.resolveCountry(requesterIp);
        if (resolvedCountry != coupon.getCountry()) {
            throw new CountryNotAllowedException(
                    "Coupon " + normalizedCode + " is not available in " + resolvedCountry
            );
        }

        CouponUsage savedUsage = couponUsageRegistrar.register(request, normalizedCode, coupon, resolvedCountry);

        log.info("Coupon {} used by user {}", normalizedCode, request.getUserId());

        int usagesLeftAfter = coupon.getMaxUsages() - (coupon.getCurrentUsages() + 1);

        return CouponUsageResponse.from(savedUsage, usagesLeftAfter);
    }
}