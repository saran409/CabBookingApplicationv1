package com.cabapp.pro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabapp.pro.entity.Cab;
import com.cabapp.pro.entity.Customer;
import com.cabapp.pro.entity.Driver;
import com.cabapp.pro.entity.Review;
import com.cabapp.pro.entity.TripBooking;
import com.cabapp.pro.exception.DriverException;
import com.cabapp.pro.repository.ICabRepository;
import com.cabapp.pro.repository.ICustomerRepository;
import com.cabapp.pro.repository.IDriverRepository;
import com.cabapp.pro.repository.IReviewRepository;
import com.cabapp.pro.repository.ITripBookingRepository;
import com.google.common.base.Objects;

@Service
public class DriverServiceImpl implements IDriverService {

	@Autowired
	IDriverRepository driverRepository;
	
	@Autowired
	ICabRepository cabRepository;
	
	@Autowired
	ITripBookingRepository tripBookingRepository;
	
	@Autowired
	ICustomerRepository customerRepository;
	
	@Autowired
	IReviewRepository reviewRepository;

	@Override
	public Driver InsertDriver(Driver driver) {
			driver.setStatus("Pending");
			Driver savedDriverInfo = driverRepository.save(driver);
			return savedDriverInfo;
	
	}

	@Override
	public List<Driver> getDriverByStatus(String status) {

		if (status != null) {
			List<Driver> finalList = driverRepository.findByStatus(status);
			return finalList;
		}
		return null;

	}

	@Override
	public Driver updateDriverByStatus(int driverId) {

		Driver driversDetails = driverRepository.getDriverByIdAndStatus(driverId);
		if(ObjectUtils.isEmpty(driversDetails))
			throw new DriverException(400,"Driver id not found ");
		List<Cab> allCabs = cabRepository.findAll();
		List<Cab> newCabByStatus = allCabs.stream().filter(e -> e.getStatus().equals("unassigned")&&e.getCurrentLocation()!=null)
				.collect(Collectors.toList());

			for(Cab b: newCabByStatus) {

			if ( b.getCurrentLocation().equals(driversDetails.getCurrentLocation())) {
				driversDetails.setStatus("ok");
					System.out.println("inside the cab to assign");
					driversDetails.setCab(b);
					b.setStatus("assigned");
					b.setDriver(driversDetails);
					driversDetails.setAvailable(true);
					cabRepository.save(b);
					return driverRepository.save(driversDetails);
				
			}

		}
		
		return null;
	}

	@Override//use separate dto to get the dtooutput....
	public TripBooking getTripBookingByCustomer(int userId) {
		//Driver driver=driverRepository.findById(userId).get();
		 Driver driver=driverRepository.findById(userId).get();
		 if(ObjectUtils.isEmpty(driver)) {
			 throw new DriverException(400,"driver id is not found in database");
		 }
		
		List<TripBooking> alltrips= new ArrayList<>();
		alltrips.addAll(driver.getTripbookings()); 
//		 System.out.println("all trips----------------"+alltrips.get(0).getFromLocation());
		 //has to be list...
		for(TripBooking t: alltrips) {
			if(t.isStatus()) {
				System.out.println("active trip----------------"+t.getDriver().getUserId());
				 System.out.println("Selected trip booking----------------"+t.getCustomer()+"----");
				 return t;
			}
		}
			throw new DriverException(404,"didnt find any active trips");
	}

	@Override
	public TripBooking tripEnd(int tripId) {
		 TripBooking tripBooking= tripBookingRepository.findById(tripId).get();
		 if(tripBooking.isStatus()) {
			 int userId=tripBooking.getDriver().getUserId(); 
			 Driver driver1=driverRepository.findById(userId).get();
			 driver1.setAvailable(true);
			 driver1.setCurrentLocation(tripBooking.getToLocation());
			 driver1.getCab().setCurrentLocation(tripBooking.getToLocation());
			 driverRepository.save(driver1);
			 tripBooking.setStatus(false);
			 tripBookingRepository.save(tripBooking); 
			 
			 return tripBooking;
			 
		 }
		 
				 
		throw new DriverException(404,"didnt find any active trip details");
	}

	@Override
	public String addReview(int driverId, Review review) {
		 Driver driver=driverRepository.findById(driverId).get();
		 if(ObjectUtils.isEmpty(driver)) {
			 throw new DriverException(404,"Driver id didnt find in db");
		 }
		 driver.getReviews().add(review);
		 
		 List<Review> reviewList  = driver.getReviews();
		 Double rating = reviewList.stream().collect(Collectors.averagingDouble(x->x.getRating() ));
		 driver.setAvgRating(rating);
		 driverRepository.save(driver);
		 
		return "Review added successfully";
	}

	@Override
	public List<Review> getReviews(int driverId) {
		 Driver driver=driverRepository.findById(driverId).get();
		 return driver.getReviews();
	}

	@Override
	public String deleteReview(int reviewId) {
		 reviewRepository.deleteById(reviewId);
		 return "review deleted";
	}
	
	

}
