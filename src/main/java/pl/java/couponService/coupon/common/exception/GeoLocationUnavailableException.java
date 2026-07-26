package pl.java.couponService.coupon.common.exception;

public class GeoLocationUnavailableException extends RuntimeException {
    public GeoLocationUnavailableException(String message) {
        super(message);
    }
    public GeoLocationUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}