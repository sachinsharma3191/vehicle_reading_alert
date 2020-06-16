package com.egen.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.egen.entity.VehicleAlert;
import com.egen.response.RestResponseBuilder;
import com.egen.service.AlertService;

@RestController
@Transactional
public class VehicleAlertController {

	@Autowired
	AlertService alertService;

	@PostMapping(value = "/alert/create")
	public ResponseEntity<Object> addAlert(@Valid @RequestBody VehicleAlert alert) {
		return RestResponseBuilder.buildResponseEntity(alertService.create(alert), "Alert Created", HttpStatus.CREATED);
	}

	@GetMapping(value = "/alert/all")
	public ResponseEntity<Object> findAllAlert() {
		List<VehicleAlert> alerts = alertService.findAll();
		if (alerts.isEmpty()) {
			return RestResponseBuilder.buildResponseEntity(new ArrayList<>(), "No Data to Fetch", HttpStatus.NOT_FOUND);
		}
		return RestResponseBuilder.buildResponseEntity(alerts, "Alerts Info Loaded", HttpStatus.OK);
	}

}
