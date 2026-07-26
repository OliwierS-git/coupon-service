package pl.java.couponService.coupon.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponCreateRequest {

    @NotBlank(message = "code is required")
    @Pattern(regexp = "^[A-Za-z0-9_-]{3,64}$", message = "code must be alphanumeric, 3-64 chars")
    private String code;

    @NotNull(message = "country is required")
    private Country country;

    @NotNull(message = "maxUsages is required")
    @Min(value = 1, message = "maxUsages must be at least 1")
    private Integer maxUsages;
}