package pl.java.couponService.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.java.couponService.coupon.common.exception.CouponNotFoundException;
import pl.java.couponService.coupon.common.web.ClientIpResolver;
import pl.java.couponService.coupon.dto.Country;
import pl.java.couponService.coupon.dto.CouponCreateRequest;
import pl.java.couponService.coupon.dto.CouponResponse;
import pl.java.couponService.usage.CouponUsageService;
import pl.java.couponService.usage.dto.CouponUsageRequest;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CouponService couponService;

    @MockitoBean
    private CouponUsageService couponUsageService;

    @MockitoBean
    private ClientIpResolver clientIpResolver;

    @Test
    void happyPath_createCoupon_returns201() throws Exception {
        var request = new CouponCreateRequest("SPRING2026", Country.PL, 100);
        var response = CouponResponse.builder().code("SPRING2026").build();
        when(couponService.createCoupon(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("SPRING2026"));
    }

    @Test
    void badPath_createCoupon_blankCode_returns400() throws Exception {
        var request = new CouponCreateRequest("", Country.PL, 100);

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void badPath_createCoupon_missingMaxUsages_returns400() throws Exception {
        String json = """
            {"code":"SPRING2026","country":"PL"}
            """;

        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void happyPath_useCoupon_returns201() throws Exception {
        var request = new CouponUsageRequest(UUID.randomUUID());
        when(clientIpResolver.resolve(any(HttpServletRequest.class))).thenReturn("8.8.8.8");
        when(couponUsageService.useCoupon(anyString(), any(), anyString())).thenReturn(null);
        mockMvc.perform(post("/api/v1/coupons/SPRING2026/usages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void badPath_useCoupon_nonExistentCode_returns404() throws Exception {
        var request = new CouponUsageRequest(UUID.randomUUID());
        when(clientIpResolver.resolve(any(HttpServletRequest.class))).thenReturn("8.8.8.8");
        when(couponUsageService.useCoupon(anyString(), any(), anyString()))
                .thenThrow(new CouponNotFoundException("NIEISTNIEJE"));


        mockMvc.perform(post("/api/v1/coupons/NIEISTNIEJE/usages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}