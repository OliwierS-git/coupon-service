package pl.java.couponService.geo;

import pl.java.couponService.coupon.dto.Country;

public interface GeoLocationClient {
    Country resolveCountry(String ipAddress);
}
