package pl.java.couponService.coupon;

import jakarta.persistence.*;
import lombok.*;
import pl.java.couponService.coupon.common.exception.CouponAlreadyUsedException;
import pl.java.couponService.coupon.dto.Country;

import java.time.Instant;
import java.util.UUID;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "coupon",
        schema = "coupons",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        }
)
public class Coupon {
    //  Globalne @Setter na encji obniża kontrolę nad zmianami stanu.
    // Propozycja: usunac @Setter z klasy i zostawic mutacje tylko przez metody.

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, updatable = false, length = 64)
    private String code;

    @Column(name = "country", nullable = false, length = 2)
    @Enumerated(EnumType.STRING)
    private Country country;

    @Column(name = "max_usages", nullable = false)
    private int maxUsages;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    @Column(name = "current_usages", nullable = false)
    private int currentUsages = 0;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public static Coupon create(String rawCode, String countryCode, int maxUsages) {
        return Coupon.builder()
                .code(normalize(rawCode))
                .country(Country.valueOf(countryCode))
                .maxUsages(maxUsages)
                .build();
    }

    private static String normalize(String rawCode) {
        return rawCode.trim().toUpperCase();
    }

    public boolean hasUsagesLeft() {
        return currentUsages < maxUsages;
    }

    public void incrementUsage() {
        if (!hasUsagesLeft()) {
            // REVIEW (rekrutacja): Semantycznie to powinien byc CouponExhaustedException.
            // Propozycja: rzucac CouponExhaustedException i zachowac CouponAlreadyUsedException
            // tylko dla konfliktu (coupon_id, user_id) konkretnego uzytkownika.
            throw new CouponAlreadyUsedException("Coupon " + code + " has no usages left");
        }
        currentUsages++;
    }
}
