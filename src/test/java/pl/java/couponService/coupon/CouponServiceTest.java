package pl.java.couponService.coupon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import pl.java.couponService.coupon.dto.Country;
import pl.java.couponService.coupon.dto.CouponCreateRequest;
import pl.java.couponService.coupon.dto.CouponResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponMapper couponMapper;

    @InjectMocks
    private CouponService couponService;

    @Test
    void happyPath_createsCouponSuccessfully() {
        var request = new CouponCreateRequest("SPRING2026", Country.PL, 100);
        var entity = Coupon.create("SPRING2026", "PL", 100);
        var expectedResponse = CouponResponse.builder().code("SPRING2026").build();

        when(couponMapper.toEntity(request)).thenReturn(entity);
        when(couponRepository.save(entity)).thenReturn(entity);
        when(couponMapper.toResponse(entity)).thenReturn(expectedResponse);

        CouponResponse result = couponService.createCoupon(request);

        assertThat(result.code()).isEqualTo("SPRING2026");
    }

    @Test
    void badPath_duplicateCode_propagatesDataIntegrityViolation() {
        var request = new CouponCreateRequest("DUPLICATE", Country.PL, 100);
        var entity = Coupon.create("DUPLICATE", "PL", 100);

        when(couponMapper.toEntity(request)).thenReturn(entity);
        when(couponRepository.save(entity)).thenThrow(new DataIntegrityViolationException("uk_coupon_code"));

        assertThatThrownBy(() -> couponService.createCoupon(request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}