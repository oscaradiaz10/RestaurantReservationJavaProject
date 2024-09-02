package restaurant.reservation.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.reservation.entity.Customer;
import restaurant.reservation.entity.Reservation;
import restaurant.reservation.entity.Restaurant;

@Data
@NoArgsConstructor
public class CustomerData {
	private Long customerId;
	private String customerName;
	private String customerPhone;
	private String customerEmail;
	private Set<CustomerRestaurant> restaurants = new HashSet<>();
	private Set<CustomerReservation> reservations = new HashSet<>();

	public CustomerData(Customer customer) {
		customerId = customer.getCustomerId();
		customerName = customer.getCustomerName();
		customerPhone = customer.getCustomerPhone();
		customerEmail = customer.getCustomerEmail();
		
		for(Restaurant restaurant : customer.getRestaurants()) {
			restaurants.add(new CustomerRestaurant(restaurant));
		}
		
		for(Reservation reservation : customer.getReservations()) {
			reservations.add(new CustomerReservation(reservation));
		}
	}
	
	@Data
	@NoArgsConstructor
	public static class CustomerRestaurant {
		private Long restaurantId;
		private String restaurantName;
		private String restaurantAddress;
		private String restaurantPhone;

		public CustomerRestaurant(Restaurant restaurant) {
			restaurantId = restaurant.getRestaurantId();
			restaurantName = restaurant.getRestaurantName();
			restaurantAddress = restaurant.getRestaurantAddress();
			restaurantPhone = restaurant.getRestaurantPhone();
		}
	}
	
	@Data
	@NoArgsConstructor
	public static class CustomerReservation {
		private Long reservationId;
		private String reservationTime;
		private String reservationDate;
		
		private Long restaurantId;

		public CustomerReservation(Reservation reservation) {
			reservationId = reservation.getReservationId();
			reservationTime = reservation.getReservationTime();
			reservationDate = reservation.getReservationDate();
			restaurantId = reservation.getRestaurant().getRestaurantId();
		}
	}
}