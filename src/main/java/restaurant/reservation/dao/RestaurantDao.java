package restaurant.reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.reservation.entity.Restaurant;

public interface RestaurantDao extends JpaRepository<Restaurant, Long> {

}