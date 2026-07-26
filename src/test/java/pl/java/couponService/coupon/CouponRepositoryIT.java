package pl.java.couponService.coupon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.java.couponService.db.AbstractIntegrationTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CouponRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void happyPath_findByCode_isCaseInsensitiveBecauseOfNormalization() {
        couponRepository.save(Coupon.create("findme", "PL", 10));

        var found = couponRepository.findByCode("FINDME");

        assertThat(found).isPresent();
    }

    @Test
    void badPath_duplicateCode_throwsDataIntegrityViolation() {
        couponRepository.saveAndFlush(Coupon.create("DUPE", "PL", 10));

        assertThrows(
                org.springframework.dao.DataIntegrityViolationException.class,
                () -> couponRepository.saveAndFlush(Coupon.create("dupe", "DE", 5))
        );
    }

    @Test
    void happyPath_tryIncrementUsage_incrementsWhenUsagesAvailable() {
        couponRepository.saveAndFlush(Coupon.create("INCR1", "PL", 5));

        int updated = couponRepository.tryIncrementUsage("INCR1");


        Coupon coupon = couponRepository.findByCode("INCR1").orElseThrow();

        assertThat(updated).isEqualTo(1);
        assertThat(coupon.getCurrentUsages()).isEqualTo(1);
    }
    @Test
    void badPath_tryIncrementUsage_returnsZeroWhenExhausted() {
        couponRepository.saveAndFlush(Coupon.create("MAXED", "PL", 1));
        couponRepository.tryIncrementUsage("MAXED");

        int secondAttempt = couponRepository.tryIncrementUsage("MAXED");

        assertThat(secondAttempt).isZero();
    }
}