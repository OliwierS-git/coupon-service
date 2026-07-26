package pl.java.couponService.coupon.common.exception;

public class CouponExhaustedException extends RuntimeException {
    public CouponExhaustedException(String message) {
        super(message);
    }
}
