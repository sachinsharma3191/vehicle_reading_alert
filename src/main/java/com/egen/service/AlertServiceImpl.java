package com.egen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egen.entity.VehicleAlert;
import com.egen.exception.AlertServicException;
import com.egen.exception.ResourceNotFoundException;
import com.egen.repository.AlertRepo;

@Service
public class AlertServiceImpl implements AlertService {

	@Autowired
	AlertRepo repo;

	@Override
	public List<VehicleAlert> findAll() {
		return repo.findAll();
	}

	@Override
	public VehicleAlert findOne(String alertId) {
		Optional<VehicleAlert> alert = repo.findById(alertId);
		if (alert.isPresent()) {
			throw new ResourceNotFoundException("No Alert found with " + alertId);
		}
		return alert.get();
	}

	@Override
	public VehicleAlert create(VehicleAlert alert) {
		try {
			return repo.save(alert);
		} catch (Exception e) {
			throw new AlertServicException("Failed to save", e.getCause());
		}
	}

}
