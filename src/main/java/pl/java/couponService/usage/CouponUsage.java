package pl.java.couponService.usage;

import jakarta.persistence.*;
import lombok.*;
import pl.java.couponService.coupon.Coupon;
import pl.java.couponService.coupon.dto.Country;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "coupon_usage",
        schema = "coupons",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"coupon_id", "user_id"})
        }
)
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coupon_id", nullable = false, updatable = false)
    private Coupon coupon;

    @Column(name = "user_id", nullable = false, updatable = false, length = 128)
    private UUID userId;

    @Builder.Default
    @Column(name = "used_at", nullable = false, updatable = false)
    private Instant usedAt = Instant.now();

    @Column(name = "country_at_usage", nullable = false, updatable = false, length = 2)
    @Enumerated(EnumType.STRING)
    private Country countryAtUsage;

    public static CouponUsage create(Coupon coupon, UUID userId, Country countryAtUsage) {
        return CouponUsage.builder()
                .coupon(coupon)
                .userId(userId)
                .countryAtUsage(countryAtUsage)
                .build();
    }
}
