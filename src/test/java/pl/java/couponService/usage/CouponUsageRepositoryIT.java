package pl.java.couponService.usage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.java.couponService.coupon.Coupon;
import pl.java.couponService.coupon.CouponRepository;
import pl.java.couponService.coupon.dto.Country;
import pl.java.couponService.db.AbstractIntegrationTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CouponUsageRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponUsageRepository couponUsageRepository;

    @Test
    void happyPath_existsByCouponIdAndUserId_returnsFalseWhenNotUsedYet() {
        Coupon coupon = couponRepository.saveAndFlush(Coupon.create("SPRING2026", "PL", 100));

        boolean exists = couponUsageRepository.existsByCouponIdAndUserId(coupon.getId(), UUID.randomUUID());

        assertThat(exists).isFalse();
    }

    @Test
    void happyPath_existsByCouponIdAndUserId_returnsTrueAfterUsage() {
        Coupon coupon = couponRepository.saveAndFlush(Coupon.create("SPRING2026", "PL", 100));
        UUID userId = UUID.randomUUID();
        couponUsageRepository.saveAndFlush(CouponUsage.create(coupon, userId, Country.PL));

        boolean exists = couponUsageRepository.existsByCouponIdAndUserId(coupon.getId(), userId);

        assertThat(exists).isTrue();
    }

    @Test
    void badPath_sameUserSameCouponTwice_throwsDataIntegrityViolation() {
        Coupon coupon = couponRepository.saveAndFlush(Coupon.create("SPRING2026", "PL", 100));
        UUID userId = UUID.randomUUID();
        couponUsageRepository.saveAndFlush(CouponUsage.create(coupon, userId, Country.PL));

        assertThrows(
                org.springframework.dao.DataIntegrityViolationException.class,
                () -> couponUsageRepository.saveAndFlush(CouponUsage.create(coupon, userId, Country.PL))
        );
    }
}