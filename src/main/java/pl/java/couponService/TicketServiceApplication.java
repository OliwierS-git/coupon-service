package pl.java.couponService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

// caly folder TARGEt nie powinien byc zacommitowany, usun to

@SpringBootApplication
@EnableConfigurationProperties
public class TicketServiceApplication {
    // Nazwa klasy "TicketServiceApplication" nie pasuje do domeny.
    // zmienic nazwe na CouponServiceApplication i zaktualizowac test contextLoads.

    public static void main(String[] args) {
        SpringApplication.run(TicketServiceApplication.class, args);
    }

}
