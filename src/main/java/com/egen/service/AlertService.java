package com.egen.service;

import java.util.List;

import com.egen.entity.VehicleAlert;

public interface AlertService {

	public List<VehicleAlert> findAll();

	public VehicleAlert findOne(String vin);

	public VehicleAlert create(VehicleAlert alert);

}
