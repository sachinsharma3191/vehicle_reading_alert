package com.egen.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egen.entity.VehicleAlert;
import com.egen.exception.AlertServicException;
import com.egen.exception.ResourceNotFoundException;
import com.egen.repository.AlertRepo;

@Service
public class AlertServiceImpl implements AlertService {

	AlertRepo repo;
	
	@Autowired
	public AlertServiceImpl(AlertRepo repo) {
		this.repo = repo;
	}

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

	@Override
	public List<VehicleAlert> findAlertsByVehicle(String vin) {
		return repo.findByVin(vin);
	}

	@Override
	public List<VehicleAlert> findAlertsByPriority(String priority) {
		List<VehicleAlert> alerts = repo.findByPriority(priority);
		if (alerts.isEmpty()) {
			return alerts;
		}
		List<VehicleAlert> sortedAlerts = alerts.stream()
				.filter(alert -> Duration.between(alert.getLastUpdated(), LocalDateTime.now()).toHours() <= 2)
				.sorted(Comparator.comparing(VehicleAlert::getVin)).collect(Collectors.toList());
		return sortedAlerts;
	}

}
