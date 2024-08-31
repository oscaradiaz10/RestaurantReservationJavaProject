package restaurant.reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.reservation.entity.Reservation;

public interface ReservationDao extends JpaRepository<Reservation, Long> {

}
