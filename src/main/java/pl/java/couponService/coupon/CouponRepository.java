package pl.java.couponService.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    // To wyszukiwanie jest case-sensitive po stronie DB.
    // Działa tylko dlatego, że logika aplikacji normalizuje kod do UPPERCASE.
    // Brak gwarancji case-insensitive na poziomie bazy.
    // Propozycja: unikalnosc i lookup opierac na LOWER(code) (np. indeks funkcjonalny),
    // albo uzyc typu CITEXT i przejsc na findByCodeIgnoreCase.
    Optional<Coupon> findByCode(String code);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Coupon c
        SET c.currentUsages = c.currentUsages + 1
        WHERE c.code = :code
          AND c.currentUsages < c.maxUsages
    """)
    int tryIncrementUsage(@Param("code") String code);
}
