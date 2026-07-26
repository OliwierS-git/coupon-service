package pl.java.couponService.usage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.java.couponService.coupon.Coupon;
import pl.java.couponService.coupon.CouponRepository;
import pl.java.couponService.coupon.common.exception.CouponAlreadyUsedException;
import pl.java.couponService.coupon.common.exception.CouponExhaustedException;
import pl.java.couponService.coupon.dto.Country;
import pl.java.couponService.usage.dto.CouponUsageRequest;

@Component
@RequiredArgsConstructor
public class CouponUsageRegistrar {

    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final CouponUsageMapper couponUsageMapper;

    @Transactional
    public CouponUsage register(CouponUsageRequest request, String normalizedCode, Coupon coupon, Country resolvedCountry) {
        if (couponUsageRepository.existsByCouponIdAndUserId(coupon.getId(), request.getUserId())) {
            throw new CouponAlreadyUsedException(
                    "User " + request.getUserId() + " already used coupon " + normalizedCode
            );
        }

        int updatedRows = couponRepository.tryIncrementUsage(normalizedCode);
        if (updatedRows == 0) {
            throw new CouponExhaustedException("Coupon " + normalizedCode + " has no usages left");
        }

        CouponUsage usage = couponUsageMapper.toEntity(request, coupon, resolvedCountry);

        try {
            return couponUsageRepository.save(usage);
        } catch (DataIntegrityViolationException e) {
            throw new CouponAlreadyUsedException(
                    "User " + request.getUserId() + " already used coupon " + normalizedCode
            );
        }
    }
}