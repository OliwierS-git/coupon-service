package pl.java.couponService.geo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "geolocation.provider")
public record GeoLocationProviderProperties(

        @NotBlank
        String baseUrl,

        @NotNull
        Duration connectTimeout,

        @NotNull
        Duration readTimeout
) {
}