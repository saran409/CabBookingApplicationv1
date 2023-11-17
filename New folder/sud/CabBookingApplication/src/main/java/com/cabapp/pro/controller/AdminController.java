package com.cabapp.pro.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cabapp.pro.entity.*;
import com.cabapp.pro.service.*;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cabapp.pro.dto.AdminRequestDTO;
import com.cabapp.pro.dto.AdminResponseDTO;
import com.cabapp.pro.dto.CabRequestDTO;
import com.cabapp.pro.dto.CabResponseDTO;
import com.cabapp.pro.dto.DriverResponseDTO;
import com.cabapp.pro.dto.TripBookingResponseByCurrentBookingDTO;
import com.cabapp.pro.dto.TripBookingResponseDTO;
import com.cabapp.pro.util.AdminDTOMapper;
import com.cabapp.pro.util.CabDTOMapper;
import com.cabapp.pro.util.DriverDTOMapper;
import com.cabapp.pro.util.TripBookingDTOMapper;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@Api
public class AdminController {
	Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	IAdminService adminService;
	
	
	@Autowired
	IDriverService driverService;
	
	
	@Autowired
	ITripBookingService tripBooking;
	
	@Autowired
	ICabService cabService;

	@Autowired
	private IComplaintService complaintService;
	
	//------------------------------------admin api's---------------------------------
	
	@PostMapping("/save") //need to test
	public ResponseEntity<AdminResponseDTO> saveAdmin(@Valid @RequestBody AdminRequestDTO dto){
		AdminDTOMapper adminConverter=new AdminDTOMapper();

		if(dto!=null) {
			logger.info("recieved request to save admin " , dto);
			Admin admin=adminConverter.getAdminFromAdminDTO(dto);
			AdminResponseDTO dto1= adminConverter.getAdminDTOFromAdmin(adminService.insertAdmin(admin));
			
			return new ResponseEntity<AdminResponseDTO>(dto1, HttpStatus.OK);
			
		}
		return null;
		
	}
	
	@PutMapping("/update")//doubt on id to get from db.......
	public ResponseEntity<AdminResponseDTO> updateAdmin(@Valid @RequestBody AdminRequestDTO dto){
		AdminDTOMapper adminConverter=new AdminDTOMapper();
		if(dto!=null) {
			logger.info("recieved request to update admin " , dto);

			Admin admin=adminConverter.getAdminFromAdminDTO(dto);
			AdminResponseDTO adminDTO= adminConverter.getAdminDTOFromAdmin(adminService.updateAdmin(admin));
			
			return new ResponseEntity<AdminResponseDTO>(adminDTO, HttpStatus.OK);
			
		}
		return null;
		
	}
	
	@GetMapping("/getall/trips/{cusId}")//doubt on retrieving all the info
	public ResponseEntity<List<TripBookingResponseDTO>> getAllTripBookings(@PathVariable int cusId){
		if(cusId!=0) {
			logger.info("recieved request to get all trip booking for customer id " , cusId);

			List<TripBooking> totalTrips=tripBooking.getAllTrips(cusId);
			TripBookingDTOMapper tripDto=new TripBookingDTOMapper();
			List<TripBookingResponseDTO> finalList=new ArrayList<>();
		    for(TripBooking t:totalTrips) {
		    	finalList.add(tripDto.getTripBookingDTOFromTripBooking(t));
		    	
		    }
		
		    
		    return new ResponseEntity<List<TripBookingResponseDTO>>(finalList, HttpStatus.OK);
		}
		return null;
	}
	

	
//------------------------------API's to access Driver------------------------------------------	
	
	@GetMapping("/get/newreq/dr/{stat}")//http://localhost:5677/admin/get/newreq/dr/Pending ....works fine
	public ResponseEntity<List<DriverResponseDTO>> getNewRegisterationRequests(@PathVariable String stat) {
		if (stat != null) {
			logger.info("recieved request for registration  " , stat);

			DriverDTOMapper driverConverter = new DriverDTOMapper();

			List<Driver> groupedByStatus = driverService.getDriverByStatus(stat);
			List<DriverResponseDTO> newDriversList=new ArrayList<>();

			for (Driver d : groupedByStatus) {
				 if(d!=null) {
					newDriversList.add(driverConverter.getDriverDTOFromDriver(d));
				 }
				
				 
			}
			return new ResponseEntity<List<DriverResponseDTO>>(newDriversList, HttpStatus.OK);

		}

		return null;
	}
	
	@PutMapping("/update/status")//working fine but clarify on the status part......
	public ResponseEntity<DriverResponseDTO> updateDriverRegisterationRequesr(@RequestParam int driId, @RequestParam String stat, @RequestParam String newStat){
		DriverDTOMapper driverConverter = new DriverDTOMapper();
		if(driId !=0 && stat!=null) {
			logger.info("recieved request to update registration " , driId);
			Driver driver=driverService.updateDriverByStatus(driId);
			DriverResponseDTO finaldriver=driverConverter.getDriverDTOFromDriver(driver);
			return new ResponseEntity<DriverResponseDTO>(finaldriver, HttpStatus.OK);
		}
		return null;
		
		
		
	}
	
	//-------------------------------------API's for cab-------------------------------
	
	
	@PostMapping("/insert/cab")
	public ResponseEntity<CabResponseDTO> saveCab(@RequestBody CabRequestDTO dto){
		CabDTOMapper cabConverter=new CabDTOMapper();
		if(dto!=null) {
			logger.info("recieved request to save cab " , dto);

			Cab cab=cabService.insertCab(cabConverter.getCabFromCabDTO(dto));
		CabResponseDTO cabDto=cabConverter.getCabDTOFromCab(cab);
		return new ResponseEntity<CabResponseDTO>(cabDto, HttpStatus.OK);
		}
		return null;
	}
	@PutMapping("/update/cab")
	public ResponseEntity<CabResponseDTO> updateCab(@Valid @RequestBody CabRequestDTO dto){
		CabDTOMapper cabConverter=new CabDTOMapper();
		if(dto!=null) {
			logger.info("recieved request to update cab " , dto);

			Cab cab=cabService.updateCab(cabConverter.getCabFromCabDTO(dto));
		CabResponseDTO newCabDto=cabConverter.getCabDTOFromCab(cab);
		return new ResponseEntity<CabResponseDTO>(newCabDto, HttpStatus.OK);
		}
		return null;
	}
	
	@DeleteMapping("/delete/cab/{cabId}")
	public ResponseEntity<String> deleteCab(@PathVariable int cabId){
		logger.info("recieved request to delete cab " , cabId);

		String message=cabService.deleteCab(cabId);
		return new ResponseEntity<String>(message , HttpStatus.OK);
	}
	
	@GetMapping("/get/OffType/{carType}")
	public ResponseEntity<List<CabResponseDTO>> getCabByType(@PathVariable String carType){
		CabDTOMapper cabConverter=new CabDTOMapper();
		if(carType!=null) {
			logger.info("recieved request to get cab by type " , carType);

			List<Cab> cab= cabService.viewCabsOfType(carType);
			List<CabResponseDTO> finalList=new ArrayList<>();
			for(Cab b:cab) {
				finalList.add(cabConverter.getCabDTOFromCab(b));
			}
			
			return new ResponseEntity<List<CabResponseDTO>>(finalList, HttpStatus.OK);
		}
		return null;
	}
	
 
	@GetMapping("/get/count/{carType}")
	public ResponseEntity<Integer> getCabCountByCarType(@PathVariable String carType){
		 
		if(carType!=null) {
			logger.info("recieved request to get cab count admin " , carType);

			Integer finalCount= cabService.countCabsOfType(carType);
			 
			 
			return new ResponseEntity<Integer>(finalCount, HttpStatus.OK);
		}
		return null;
	}
	@GetMapping("/cabby/{cId}")
	public ResponseEntity<CabResponseDTO> getCabById(@PathVariable int cId)  {
		if(cId!=0) {
			CabDTOMapper converter=new CabDTOMapper();
			CabResponseDTO cab=converter.getCabDTOFromCab(cabService.viewCab(cId));
				 
		
		return new ResponseEntity<CabResponseDTO>(cab , HttpStatus.OK);
		}
		return null;
		
	}
		
		
	
	@GetMapping("/allcabs") /// www.localhost:5678/cab/allcabs
	public ResponseEntity<List<CabResponseDTO>> getAllCabs(){
		CabDTOMapper converter=new CabDTOMapper();
		System.out.println("inside controller getCabs()");
		List<Cab> temp = cabService.viewAllCab();
		List<CabResponseDTO> finalCabs=new ArrayList<>();
		for(Cab b: temp) {
			finalCabs.add(converter.getCabDTOFromCab(b));
		}
		
		return new ResponseEntity<List<CabResponseDTO>>(finalCabs, HttpStatus.OK);
	}
//	--------------------------------gettripscabwise-------------------	
	
	@GetMapping("/get/trips/bycustomerwise")
	public ResponseEntity<List<TripBookingResponseDTO>> getTripByCustomerOrder(){
		TripBookingDTOMapper tripConverter=new TripBookingDTOMapper();
		 
			List<TripBooking> trips= adminService.getTripsCustomerwise();
			List<TripBookingResponseDTO> finalList=new ArrayList<>();
			for(TripBooking b:trips) {
				finalList.add(tripConverter.getTripBookingDTOFromTripBooking(b));
			}
			
			return new ResponseEntity<List<TripBookingResponseDTO>>(finalList, HttpStatus.OK);
		}
	//-------------------------------------gettripbycabwise----------------------
	
	@GetMapping("/get/trips/bycabwise")
	public ResponseEntity<Map<Integer, List<TripBooking>>> getTripByCabwise(){
		 
		 
		Map<Integer, List<TripBooking>> trips= adminService.getTripsCabwise();
			 
			return new ResponseEntity<Map<Integer, List<TripBooking>>>(trips, HttpStatus.OK);
		}
		 //-----------------------------gattripsdatewise--------------
	
	@GetMapping("/get/trips/bydatewise")
	public ResponseEntity<List<TripBooking>> getTripByDateOrder(){
		//TripBookingDTOMapper tripConverter=new TripBookingDTOMapper();
		 
			List<TripBooking> trips= adminService.getTripsDatewise();
			//List<TripBookingResponseDTO> finalList=new ArrayList<>();
			//for(TripBooking b:trips) {
			//	finalList.add(tripConverter.getTripBookingDTOFromTripBooking(b));
			//}
			
			return new ResponseEntity<List<TripBooking>>(trips, HttpStatus.OK);
		}
	
	//========================================gettripbyfromtodate--------------------

	@GetMapping("/get/trips/bydate/range")//doubt may be need to cahnge the localdatetime class
	public ResponseEntity<List<TripBooking>> getTripByDays(@RequestParam int custId,@RequestParam LocalDate fromdat, @RequestParam LocalDate todate ){
		//TripBookingDTOMapper tripConverter=new TripBookingDTOMapper();
		 
			List<TripBooking> trips= adminService.getAllTripsForDays(custId,fromdat,todate);
			//List<TripBookingResponseDTO> finalList=new ArrayList<>();
			//for(TripBooking b:trips) {
			//	finalList.add(tripConverter.getTripBookingDTOFromTripBooking(b));
			//}
			
			return new ResponseEntity<List<TripBooking>>(trips, HttpStatus.OK);
		}

	//-----------------------------------getcurrentBooking--------------------------------
		@GetMapping("/currenttrip/getBy/{userId}")//not working
		public ResponseEntity<TripBookingResponseByCurrentBookingDTO> getTripBookingById( @PathVariable int  userId)
		{
			     if(userId!=0) {
			    	 TripBookingDTOMapper converter=new TripBookingDTOMapper();
			
			
			    	 TripBookingResponseByCurrentBookingDTO dto=converter.getTripBookingDTOFromTripBookingByCurrentBooking(adminService.getTripBookingById(userId));
			     return new ResponseEntity<TripBookingResponseByCurrentBookingDTO>(dto,HttpStatus.OK);
			    
		}
			     return null;
		}
// =====================================Complaint related code ==================================================

	@GetMapping("/complaints/status/{status}")
	public ResponseEntity<Object> findComplaintsByStatus(@PathVariable("status") String status) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(complaintService.complaintByStatus(status));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);

		}
	}

	@GetMapping("/complaints/all")
	public ResponseEntity<Object> getAllCompalints() {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(complaintService.getAllComplaints());
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);

		}
	}

	@PutMapping("/complaints/update")
	public ResponseEntity<Object> updateComplaint(@Valid @RequestBody Complaint complaint) {
		try {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(complaintService.updateComplaint(complaint));
		} catch(Exception exception) {
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);

		}
	}
	
	// Delete Review by id
	
	@DeleteMapping("/delete/review")
	public ResponseEntity<Object> deleteReview(@RequestParam("id") Integer reviewId) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(driverService.deleteReview(reviewId));

	}
}
	
	


