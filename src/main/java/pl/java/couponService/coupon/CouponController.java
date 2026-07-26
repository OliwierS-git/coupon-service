package pl.java.couponService.coupon;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.java.couponService.coupon.common.web.ClientIpResolver;
import pl.java.couponService.coupon.dto.CouponCreateRequest;
import pl.java.couponService.coupon.dto.CouponResponse;
import pl.java.couponService.usage.CouponUsageService;
import pl.java.couponService.usage.dto.CouponUsageRequest;
import pl.java.couponService.usage.dto.CouponUsageResponse;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
    private final CouponUsageService couponUsageService;
    private final ClientIpResolver clientIpResolver;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CouponCreateRequest request) {
        CouponResponse response = couponService.createCoupon(request);
        return ResponseEntity.status(201).body(response);
    }
    @PostMapping("/{code}/usages")
    public ResponseEntity<CouponUsageResponse> useCoupon(
            @PathVariable String code,
            @Valid @RequestBody CouponUsageRequest request,
            HttpServletRequest httpRequest
    ) {
        String clientIp = clientIpResolver.resolve(httpRequest);
        CouponUsageResponse response = couponUsageService.useCoupon(code, request, clientIp);
        return ResponseEntity.status(201).body(response);
    }
}
