package com.cabapp.pro.service;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabapp.pro.entity.Cab;

import com.cabapp.pro.repository.ICabRepository;
import com.cabapp.pro.repository.ITripBookingRepository;

@Service
public class CabServiceImpl implements ICabService {

	@Autowired
	ICabRepository cabRepo;

	@Autowired
	ITripBookingRepository tripBooking;

	@Override
	public Cab insertCab(Cab cab) {
		if (cab != null) {
			{
				cab.setStatus("unassigned");
				return cabRepo.save(cab);
			}

		}
		return null;
	}

	@Override
	public Cab updateCab(Cab cab) {

		if (cab != null) {
			int id = (int) cab.getCabId();
			Cab cabById = cabRepo.findById(id).get();
			if (cabById != null) {
				if (cab.getCarType() != null) {
					cabById.setCarType(cab.getCarType());
				}
				if (cab.getPerKmRate() != 0) {
					cabById.setPerKmRate(cab.getPerKmRate());
				}
				if (cab.getStatus() != null) {
					cabById.setStatus(cab.getStatus());
				}

				return cabRepo.save(cabById);
			}

		}

		return null;
	}

	@Override
	public String deleteCab(int cabId) {
		if (cabId != 0) {
			cabRepo.deleteById(cabId);
			return "deleted";
		}
		return null;
	}

	@Override
	public List<Cab> viewCabsOfType(String carType) {
		if (carType != null) {
			return cabRepo.getCabByCarType(carType);
		}
		return null;
	}

	@Override
	public Integer countCabsOfType(String carType) {
		if (carType != null) {
			List<Cab> CabsByType = cabRepo.getCabByCarType(carType);

			return CabsByType.size();
		}
		return null;
	}

	@Override
	public Cab viewCab(int cabId) {
		Cab savedCab = cabRepo.findById(cabId).get();
		if (savedCab != null) {
			return savedCab;
		} else
			return null;

	}

	@Override
	public List<Cab> viewAllCab() {
		List<Cab> cabs = cabRepo.findAll();
		if (cabs != null) {
			return cabs;
		}
		return null;
	}
}
