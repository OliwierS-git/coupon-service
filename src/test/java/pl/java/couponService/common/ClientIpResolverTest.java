package pl.java.couponService.common;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import pl.java.couponService.coupon.common.web.ClientIpResolver;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientIpResolverTest {
    private final ClientIpResolver resolver = new ClientIpResolver();

    @Test
    void happyPath_returnsFirstIpFromForwardedForHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn("1.2.3.4, 5.6.7.8, 9.10.11.12");

        String result = resolver.resolve(request);

        assertThat(result).isEqualTo("1.2.3.4");
    }

    @Test
    void badPath_headerAbsent_fallsBackToRemoteAddr() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");

        String result = resolver.resolve(request);

        assertThat(result).isEqualTo("10.0.0.1");
    }

    @Test
    void badPath_headerBlank_fallsBackToRemoteAddr() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn("   ");
        when(request.getRemoteAddr()).thenReturn("10.0.0.2");

        String result = resolver.resolve(request);

        assertThat(result).isEqualTo("10.0.0.2");
    }
}