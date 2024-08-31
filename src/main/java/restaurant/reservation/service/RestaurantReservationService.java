package restaurant.reservation.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import restaurant.reservation.controller.model.CustomerData;
import restaurant.reservation.controller.model.CustomerData.CustomerReservation;
import restaurant.reservation.controller.model.CustomerData.CustomerRestaurant;
import restaurant.reservation.dao.CustomerDao;
import restaurant.reservation.dao.ReservationDao;
import restaurant.reservation.dao.RestaurantDao;
import restaurant.reservation.entity.Customer;
import restaurant.reservation.entity.Reservation;
import restaurant.reservation.entity.Restaurant;

@Service
public class RestaurantReservationService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private RestaurantDao restaurantDao;

    @Transactional(readOnly = false)
	public CustomerData saveCustomer(CustomerData customerData) {
		Long customerId = customerData.getCustomerId();
		Customer customer;

	    if (Objects.isNull(customerId) || !customerDao.existsById(customerId)) {
	        customer = new Customer();
	    } else {
	        customer = findCustomerById(customerId);
	    }

	    copyCustomerFields(customer, customerData);
	    return new CustomerData(customerDao.save(customer));
    }
    
    private void copyCustomerFields(Customer customer, CustomerData customerData) {
    	customer.setCustomerId(customerData.getCustomerId());
    	customer.setCustomerName(customerData.getCustomerName());
    	customer.setCustomerPhone(customerData.getCustomerPhone());
    	customer.setCustomerEmail(customerData.getCustomerEmail());
    }
    
    public Customer findCustomerById(Long customerId) {
    	System.out.println("Finding customer with ID=" + customerId);
        return customerDao.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer with ID=" + customerId + " was not found."));
    }
    
    @Transactional(readOnly = true)
    public List<CustomerData> retrieveAllCustomers() {
        List<Customer> customers = customerDao.findAll();
        
        List<CustomerData> result = new LinkedList<>();
        
        for (Customer customer : customers) {
        	CustomerData cd = new CustomerData(customer);
        	
        	cd.getReservations().clear();
        	cd.getRestaurants().clear();
            
            result.add(cd);
        }

        return result;
    }
    
    private Restaurant findOrCreateRestaurant(Long restaurantId) {
        if (Objects.isNull(restaurantId)) {
            return new Restaurant();
        }

        return findRestaurantById(restaurantId, restaurantId);
    }
    
    private Restaurant findRestaurantById(Long restaurantId, Long customerId) {
    	Restaurant restaurant = restaurantDao.findById(restaurantId)
                .orElseThrow(() -> new NoSuchElementException("Restaurant with ID=" + restaurantId + " not found"));

        boolean found = false;

        for(Customer customer : restaurant.getCustomers()) {
        	if(customer.getCustomerId() == customerId) {
        		found = true;
        		break;
        	}
        }
        
        if(!found) {
        	throw new IllegalArgumentException("Restaurant with ID=" + restaurantId + " does not have a reservation of cusomter with ID=" + customerId);
        }

        return restaurant;
    }
    
    private Reservation findReservationById(Long customerId, Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new NoSuchElementException("Reservation with ID=" + reservationId + " was not found"));

        if(reservation.getCustomer().getCustomerId() != customerId) {
            throw new IllegalArgumentException("Reservation does not belong to the Customer with ID=" + customerId);
        }

        return reservation;
    }
    
    private void copyReservationFields(Reservation reservation, CustomerReservation customerReservation) {
    	reservation.setReservationId(customerReservation.getReservationId());
    	reservation.setReservationTime(customerReservation.getReservationTime());
    	reservation.setReservationDate(customerReservation.getReservationDate());
    }
    
    private void copyRestaurantFields(Restaurant restaurant, CustomerRestaurant customerRestaurant) {
    	restaurant.setRestaurantId(customerRestaurant.getRestaurantId());
    	restaurant.setRestaurantName(customerRestaurant.getRestaurantName());
    	restaurant.setRestaurantAddress(customerRestaurant.getRestaurantAddress());
    	restaurant.setRestaurantPhone(customerRestaurant.getRestaurantPhone());
    }
    
    private Reservation findOrCreateReservation(Long customerId, Long reservationId) {
        if(Objects.isNull(reservationId)) {
            return new Reservation();
        }
        
        return findReservationById(reservationId, customerId);
    }
    
    @Transactional(readOnly = false)
    public CustomerReservation saveReservation(Long customerId, Long restaurantId, CustomerReservation customerReservation) {
        Customer customer = findCustomerById(customerId);
        Restaurant restaurant = findRestaurantById(customerId, restaurantId);
        Long reservationId = customerReservation.getReservationId();
        Reservation reservation = findOrCreateReservation(customerId, reservationId);
        
        copyReservationFields(reservation, customerReservation);
        
        reservation.setCustomer(customer);
        reservation.setRestaurant(restaurant);  // Ensure the restaurant is set
        customer.getReservations().add(reservation);
        
        Reservation dbReservation = reservationDao.save(reservation);

        return new CustomerReservation(dbReservation);
    }
    
    @Transactional(readOnly = false)
    public CustomerRestaurant saveRestaurant(Long customerId, CustomerRestaurant customerRestaurant) {
        Customer customer = findCustomerById(customerId);
        Restaurant restaurant = findOrCreateRestaurant(customerId);

        copyRestaurantFields(restaurant, customerRestaurant);

        restaurant.getCustomers().add(customer);
        customer.getRestaurants().add(restaurant);

        Restaurant dbRestaurant = restaurantDao.save(restaurant);

        return new CustomerRestaurant(dbRestaurant);
    }
    
    @Transactional(readOnly = true)
	public CustomerData retrieveCustomerById(Long customerId) {
		return new CustomerData(findCustomerById(customerId));
	}
	
	@Transactional(readOnly = false)
    public void deleteCustomerById(Long customerId) {
        Customer customer = findCustomerById(customerId);
        customerDao.delete(customer);
    }
	
	public void addReservation(Customer customer, Reservation reservation) {
	    customer.getReservations().add(reservation);
	    reservation.setCustomer(customer);
	}

	public CustomerReservation saveReservation(Long customerId, CustomerReservation customerReservation) {
		// TODO Auto-generated method stub
		return null;
	}
}
