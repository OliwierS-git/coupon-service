package pl.java.couponService.coupon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.java.couponService.coupon.dto.CouponCreateRequest;
import pl.java.couponService.coupon.dto.CouponResponse;
@Service
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    public CouponService(CouponRepository couponRepository, CouponMapper couponMapper) {
        this.couponRepository = couponRepository;
        this.couponMapper = couponMapper;
    }

    public CouponResponse createCoupon(CouponCreateRequest request) {
        Coupon coupon = couponMapper.toEntity(request);
        Coupon saved = couponRepository.save(coupon);
        log.info("Created coupon code={} maxUsages={}", saved.getCode(), saved.getMaxUsages());
        return couponMapper.toResponse(saved);
    }
}