package pl.java.couponService.coupon.common.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class ClientIpResolver {

    private static final String FORWARDED_FOR_HEADER = "X-Forwarded-For";

    public String resolve(HttpServletRequest request) {
        String forwardedFor = request.getHeader(FORWARDED_FOR_HEADER);
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            // Nagłówek X-Forwarded-For jest tutaj bezwarunkowo
            // zaufany. Bez kontroli zaufanych proxy klient może go spoofować i obchodzić
            // ograniczenie kraju.
            // Propozycja: honorowac X-Forwarded-For tylko za zaufanym proxy/load balancerem
            // (whitelista source IP) albo delegowac to do ForwardedHeaderFilter + infra.
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
