package pl.java.couponService.usage.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponUsageRequest {
    // RNazwa pakietu zawiera camelCase (`couponService`),
    // co jest niezgodne z konwencja Javy.
    // Propozycja: przy okazji refaktoru zmienic pakiety na lowercase (`couponservice`).

    @NotNull(message = "userId is required")
    private UUID userId;

    //  "Martwy" konstruktor nic nie ustawia i moze tworzyc
    // obiekt z userId == null.
    // Propozycja: usunac ten konstruktor; jesli jest potrzebny overload, to musi
    // delegowac do this(userId) i ustawic wszystkie pola jawnie.
    public CouponUsageRequest(UUID userId, String spring2026) {
    }
}
