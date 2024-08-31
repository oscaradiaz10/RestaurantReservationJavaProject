package restaurant.reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.reservation.entity.Customer;

public interface CustomerDao extends JpaRepository<Customer, Long> {

}
