package pl.java.couponService.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pl.java.couponService.coupon.common.exception.GeoLocationUnavailableException;
import pl.java.couponService.coupon.dto.Country;
import pl.java.couponService.geo.dto.IpApiResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeoLocationService implements GeoLocationClient {

    private final RestClient geoLocationRestClient;

    @Override
    public Country resolveCountry(String ipAddress) {
        IpApiResponse response;
        try {
            response = geoLocationRestClient.get()
                    .uri("/json/{ip}?fields=status,message,countryCode", ipAddress)
                    .retrieve()
                    .body(IpApiResponse.class);
        } catch (RestClientException e) {
            log.warn("Geolocation lookup failed for IP {}: {}", ipAddress, e.getMessage());
            throw new GeoLocationUnavailableException("Geolocation service unavailable", e);
        }

        if (response == null || !"success".equals(response.status())) {
            String reason = response != null ? response.message() : "no response";
            log.warn("Geolocation lookup failed for IP {}: {}", ipAddress, reason);
            throw new GeoLocationUnavailableException("Could not resolve country for IP " + ipAddress);
        }

        try {
            return Country.valueOf(response.countryCode());
        } catch (IllegalArgumentException e) {
            log.warn("Geolocation returned unsupported country code '{}' for IP {}", response.countryCode(), ipAddress);
            throw new GeoLocationUnavailableException(
                    "Country " + response.countryCode() + " is not supported", e);
        }
    }
}