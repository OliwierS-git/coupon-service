package pl.java.couponService.coupon.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiInfo {

    private HttpStatus httpStatus;
    private String error;
    private LocalDateTime timestamp;

}