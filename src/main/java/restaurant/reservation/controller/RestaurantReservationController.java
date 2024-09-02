package restaurant.reservation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import restaurant.reservation.controller.model.CustomerData;
import restaurant.reservation.controller.model.CustomerData.CustomerReservation;
import restaurant.reservation.controller.model.CustomerData.CustomerRestaurant;
import restaurant.reservation.service.RestaurantReservationService;

@RestController
@RequestMapping("/restaurant_reservation")
@Slf4j
public class RestaurantReservationController {
    @Autowired
    private RestaurantReservationService restaurantReservationService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED) // Create 201
    public CustomerData insertCustomer(@RequestBody CustomerData customerData) {
		log.info("Creating customer {}", customerData);
		return restaurantReservationService.saveCustomer(customerData);
	}
    
    @PutMapping("/{customerId}") // Update
	public CustomerData updateCustomer(@PathVariable Long customerId, @RequestBody CustomerData customerData) {
		customerData.setCustomerId(customerId);
		log.info("Updating customer {}", customerData);
		return restaurantReservationService.saveCustomer(customerData);
	}
    
    @PostMapping("/{customerId}/reservation")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CustomerReservation addReservationToCustomer(@PathVariable Long customerId, @RequestBody CustomerReservation customerReservation) {
    	log.info("Received request to add reservation for customerId: {}", customerReservation, customerId);
        log.info("Request payload: {}", customerReservation);
        return restaurantReservationService.saveReservation(customerId, customerReservation);
    }
    
    @PostMapping("/{customerId}/restaurant")
	@ResponseStatus(code = HttpStatus.CREATED)
	public CustomerRestaurant addRestaurantToCustomer(@PathVariable Long customerId, @RequestBody CustomerRestaurant customerRestaurant) {
		log.info("Adding restaurant {} to customer with ID {}", customerRestaurant, customerId);
	    return restaurantReservationService.saveRestaurant(customerId, customerRestaurant);
	}
    
    @GetMapping
    public List<CustomerData> getAllCustomers() {
        log.info("Getting all customers");
        return restaurantReservationService.retrieveAllCustomers();
    }
	
	@GetMapping("/{customerId}")
	public CustomerData retrieveCustomerById(@PathVariable Long customerId) {
		log.info("Retrieving customer with ID={}", customerId);
		return restaurantReservationService.retrieveCustomerById(customerId);
	}
	
	@DeleteMapping("/{customerId}")
    public Map<String, String> deleteCustomerById(@PathVariable Long customerId) {
		log.info("Deleting customer with ID={}", customerId);
		
		restaurantReservationService.deleteCustomerById(customerId);
		
		return Map.of("message", "Deletion of customer with ID=" + customerId + " was successful.");
	}
}
