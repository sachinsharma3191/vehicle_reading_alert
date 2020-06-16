package com.egen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egen.entity.VehicleAlert;

@Repository
public interface AlertRepo extends JpaRepository<VehicleAlert, String> {

}
