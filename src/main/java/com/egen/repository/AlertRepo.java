package com.egen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egen.entity.VehicleAlert;

@Repository
public interface AlertRepo extends JpaRepository<VehicleAlert, String> {

	public List<VehicleAlert> findByVin(String vin);

	public List<VehicleAlert> findByPriority(String priority);
}
