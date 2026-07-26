package pl.java.couponService.geo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pl.java.couponService.coupon.common.exception.GeoLocationUnavailableException;
import pl.java.couponService.geo.dto.IpApiResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeoLocationServiceTest {

    @Mock
    private RestClient restClient;
    @Mock
    private RestClient.RequestHeadersUriSpec uriSpec;
    @Mock
    private RestClient.ResponseSpec responseSpec;

    private GeoLocationService geoLocationService;

    @Test
    void happyPath_returnsCountryOnSuccessfulLookup() {
        setupMockChain(new IpApiResponse("success", "PL", null));
        geoLocationService = new GeoLocationService(restClient);

        var result = geoLocationService.resolveCountry("8.8.8.8");

        assertThat(result.name()).isEqualTo("PL");
    }

    @Test
    void badPath_apiReturnsFailStatus_throwsGeoLocationUnavailable() {
        setupMockChain(new IpApiResponse("fail", "private range", null));
        geoLocationService = new GeoLocationService(restClient);

        assertThatThrownBy(() -> geoLocationService.resolveCountry("127.0.0.1"))
                .isInstanceOf(GeoLocationUnavailableException.class);
    }

    @Test
    void badPath_httpCallThrows_wrapsInGeoLocationUnavailable() {
        when(restClient.get()).thenThrow(new RestClientException("timeout"));
        geoLocationService = new GeoLocationService(restClient);

        assertThatThrownBy(() -> geoLocationService.resolveCountry("8.8.8.8"))
                .isInstanceOf(GeoLocationUnavailableException.class);
    }

    @SuppressWarnings("unchecked")
    private void setupMockChain(IpApiResponse response) {
        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), any(Object[].class))).thenReturn(uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(IpApiResponse.class)).thenReturn(response);
    }
}