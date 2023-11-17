package com.cabapp.pro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cabapp.pro.entity.Cab;

@Service
public interface ICabService {
	
	public Cab insertCab(Cab cab);
	public Cab updateCab(Cab cab);
	public String deleteCab(int cabId);
	public List<Cab> viewCabsOfType(String carType);
	public Integer countCabsOfType(String carType);
	public Cab viewCab(int cabId);
	public List<Cab> viewAllCab();

}
