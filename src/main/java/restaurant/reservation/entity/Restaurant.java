package restaurant.reservation.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantPhone;

    @ManyToMany(mappedBy = "restaurants", cascade = CascadeType.PERSIST)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Reservation> reservations = new HashSet<>();
}
