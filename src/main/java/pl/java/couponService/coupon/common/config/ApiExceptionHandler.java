package pl.java.couponService.coupon.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.java.couponService.coupon.common.exception.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ApiInfo> handleCouponNotFoundException(HttpServletRequest req, CouponNotFoundException e) {
        ApiInfo apiInfo = new ApiInfo(HttpStatus.NOT_FOUND, e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CountryNotAllowedException.class)
    public ResponseEntity<ApiInfo> handleCountryNotAllowedException(HttpServletRequest req, CountryNotAllowedException e) {
        ApiInfo apiInfo = new ApiInfo(HttpStatus.FORBIDDEN, e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiInfo, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CouponAlreadyUsedException.class)
    public ResponseEntity<ApiInfo> handleCouponAlreadyUsedException(HttpServletRequest req, CouponAlreadyUsedException e) {
        ApiInfo apiInfo = new ApiInfo(HttpStatus.CONFLICT, e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiInfo, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CouponExhaustedException.class)
    public ResponseEntity<ApiInfo> handleCouponExhaustedException(HttpServletRequest req, CouponExhaustedException e) {
        ApiInfo apiInfo = new ApiInfo(HttpStatus.CONFLICT, e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiInfo, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GeoLocationUnavailableException.class)
    public ResponseEntity<ApiInfo> handleGeoLocationUnavailableException(HttpServletRequest req, GeoLocationUnavailableException e) {
        ApiInfo apiInfo = new ApiInfo(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiInfo, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiInfo> handleValidationException(HttpServletRequest req, MethodArgumentNotValidException e) {
        String detail = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        ApiInfo apiInfo = new ApiInfo(HttpStatus.BAD_REQUEST, detail, LocalDateTime.now());
        return new ResponseEntity<>(apiInfo, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiInfo> handleDataIntegrityViolation(HttpServletRequest req, DataIntegrityViolationException e) {
        // Każde naruszenie integralności jest mapowane na
        // "Coupon code already exists", co maskuje inne błędy danych.
        // Propozycja: rozróżnić naruszenia po nazwie constraintu i zwracać
        // dedykowany komunikat/kod dla każdego scenariusza.
        ApiInfo apiInfo = new ApiInfo(HttpStatus.CONFLICT, "Coupon code already exists", LocalDateTime.now());
        return new ResponseEntity<>(apiInfo, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiInfo> handleUnreadableMessage(HttpServletRequest req, HttpMessageNotReadableException e) {
        ApiInfo apiInfo = new ApiInfo(HttpStatus.BAD_REQUEST, "Malformed request body", LocalDateTime.now());
        return new ResponseEntity<>(apiInfo, HttpStatus.BAD_REQUEST);
    }
}
