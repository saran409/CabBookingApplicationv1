package com.cabapp.pro.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cabapp.pro.dto.CabResponseDTO;
import com.cabapp.pro.dto.CustomerRequestSubmitDTO;
import com.cabapp.pro.dto.CustomerResponseDTO;
import com.cabapp.pro.dto.TripBookingRequestSubmitDTO;
import com.cabapp.pro.dto.TripBookingResponseDTO;
import com.cabapp.pro.entity.Cab;
import com.cabapp.pro.entity.Complaint;
import com.cabapp.pro.entity.Customer;
import com.cabapp.pro.entity.Review;
import com.cabapp.pro.entity.TripBooking;
import com.cabapp.pro.service.IComplaintService;
import com.cabapp.pro.service.ICustomerService;
import com.cabapp.pro.service.IDriverService;
import com.cabapp.pro.service.ITripBookingService;
import com.cabapp.pro.util.CabDTOMapper;
import com.cabapp.pro.util.CustomerDTOMapper;
import com.cabapp.pro.util.TripBookingDTOMapper;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/customer")
@Validated
@Api
public class CustomerController {
	
	@Autowired
	ICustomerService service;
	
	@Autowired
	ITripBookingService tripService;
	
	@Autowired
	IDriverService driverService;
	
	@Autowired
	IComplaintService complaintService;
	

	public CustomerController() {
		 System.out.println("Inside Customer controller");
	}
	
	
	@GetMapping("/get")
	public String getStatement() {
		return "all the points to be noted.....";
	}
	
	@PostMapping("/insert/customer")
	public CustomerResponseDTO saveCustomer(@Valid @RequestBody CustomerRequestSubmitDTO dto)  {
		if(dto!=null) {
			CustomerDTOMapper dtoConverter=new CustomerDTOMapper();
			Customer customer=dtoConverter.getCustomerFromCustomerDTO(dto);
			
		
		 
		Customer savedInfo = service.insertCustomer(customer);
		return dtoConverter.getCustomerDTOFromCustomer(savedInfo);
	}
		else {
			
			return null;
		
		}
	}
			 
	
	
	
	
	@GetMapping("/customerby/{cId}")
	public CustomerResponseDTO getCustomerById(@PathVariable int cId)  {
		
		
		CustomerDTOMapper dtoConverter=new CustomerDTOMapper();
		return dtoConverter.getCustomerDTOFromCustomer(service.viewCustomer(cId));
			 
		
	}
	
	
	@GetMapping("/allcustomers") /// www.localhost:5643/customer/allcustomers
	public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
		System.out.println("inside controller getCustomers()");
		CustomerDTOMapper dtoConverter=new CustomerDTOMapper();
		
 
		List<Customer> savedCustomers=service.viewCustomers();
		List<CustomerResponseDTO> finalList = new ArrayList<>();
		 for(Customer cu:savedCustomers) {
			 finalList.add(dtoConverter.getCustomerDTOFromCustomer(cu));
		 }
		return new ResponseEntity<List<CustomerResponseDTO>>(finalList, HttpStatus.OK);
	}
	
	
	
	
	
	@GetMapping("/validate")
	public ResponseEntity<Customer> validateCustomer(@RequestParam String userName, @RequestParam String password) {
		Customer savedCustomer = service.validateCustomer(userName, password);
		Customer e = new Customer();
		if (savedCustomer != null) {
			return new ResponseEntity<Customer>(savedCustomer, HttpStatus.OK);
		} else
			return new ResponseEntity<Customer>(e, HttpStatus.NOT_FOUND);
	}
	
	
		
	@PutMapping("/update/{id}")//need to update refer admin update.......
	public CustomerResponseDTO updateCustomer(@RequestBody Customer customer, @PathVariable Integer id) {
		
		CustomerDTOMapper dtoConverter=new CustomerDTOMapper();
		return dtoConverter.getCustomerDTOFromCustomer(service.updateCustomer(customer, id));
	}
	
	
	@DeleteMapping("/delete/{id}")
	public CustomerResponseDTO deleteCustomerById(@PathVariable int id) {
		CustomerDTOMapper dtoConverter=new CustomerDTOMapper();
		CustomerResponseDTO deletedCustomer= dtoConverter.getCustomerDTOFromCustomer(service.deleteCustomer(id));
		return deletedCustomer;
	}
	
	//----------------------------tripbooking API's------------------
	
	@GetMapping("/searchcab/{currentLocation}")
	public ResponseEntity<List<CabResponseDTO> >searchCabBasedOnLocations(@PathVariable String currentLocation){
		List<Cab> allCabs = service.getCabByLocations(currentLocation);
		CabDTOMapper converter=new CabDTOMapper();
		List<CabResponseDTO> cabs=new ArrayList<>();
		for(Cab b:allCabs) {
			cabs.add(converter.getCabDTOFromCab(b));
		}
		
		return new ResponseEntity<List<CabResponseDTO>>(cabs,HttpStatus.OK);
	}
	
	
	@PostMapping("/trip")
	public TripBookingResponseDTO addTrip(@Valid @RequestBody TripBookingRequestSubmitDTO trip)
	{
		     TripBookingDTOMapper converter=new TripBookingDTOMapper();
		     
		     TripBooking tripBooking=converter.getTripBookingFromTripBookingDTO(trip );
		
		
		    return converter.getTripBookingDTOFromTripBooking(tripService.insertTripBooking(tripBooking));
		     
		    
	}//has to be tested ...................
	
	
	@GetMapping("/trip/getBy/{userId}")
	public TripBookingResponseDTO getCurrentTripByUserId( @PathVariable int  userId)
	{
		     if(userId!=0) {
		    	 TripBookingDTOMapper converter=new TripBookingDTOMapper();
		
		
		    return converter.getTripBookingDTOFromTripBooking(service.getTripBookingByUserId(userId));
		     
		    
	}
		     return null;
	}
	
	
	
	
	@GetMapping("/getalltrips")
	public ResponseEntity<List<TripBooking>> getAllTripBooking(@RequestParam int custId) {
		List<TripBooking> savedCustomer = tripService.viewAllTripsCustomer(custId);
		//TripBooking e = new TripBooking();
		if (savedCustomer != null) {
			return new ResponseEntity<List<TripBooking>>(savedCustomer, HttpStatus.OK);
		} 
		return null;//need to test this method....
	}
	
	
	//not needed just cancel the whole trip
	@PutMapping("/update/booking")//create one form in the frontend including the customerId//fornow on hold lets see in future
	public TripBooking updateBooking(@RequestBody TripBooking tripBooking) {
		
		 if(tripBooking!=null) {
		return tripService.updateTripBooking( tripBooking);
		 }
		 return null;
	}
	
	@DeleteMapping
    public TripBooking cancelBooking(@PathVariable int tripId) {
    	if(tripId!=0) {
    		return tripService.deleteTripBooking(tripId);
    	}
    	
    	
    	return null;
    }
	
	
	
	//--------------------------------------------complaint
	
	@PostMapping("/complaint/save")
	    public ResponseEntity<Object> saveComplaint(@RequestBody Complaint complaint) {
	        try {
	            return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.createComplaint(complaint));
	        } catch (Exception exception) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
	        }
	 
	    }
	 
	 
	    @GetMapping("/complaintByUser/{id}")
	    public ResponseEntity<Object> findComplaintsByUser(@PathVariable("id") int userId) {
	        try {
	            return ResponseEntity.status(HttpStatus.OK).body(complaintService.complaintByUser(userId));
	        } catch(Exception exception) {
	            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
	 
	        }
	    }
	
	//-----------------------------------------------------------------------------------------------------
	
	// Add reviews to driver
	@PostMapping("/add/review")
	public String addReview(@RequestParam("driverId") int driverId,  @RequestBody Review review) {
		return driverService.addReview(driverId, review);
	}

		
	}
	
	
	
	

