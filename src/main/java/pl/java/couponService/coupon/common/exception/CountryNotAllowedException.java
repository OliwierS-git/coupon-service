package pl.java.couponService.coupon.common.exception;

public class CountryNotAllowedException extends RuntimeException {
    public CountryNotAllowedException(String message) {
        super(message);
    }
}