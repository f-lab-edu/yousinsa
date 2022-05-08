package com.flab.yousinsa.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flab.yousinsa.applications.SampleService;
import com.flab.yousinsa.interfaces.dto.SampleDto;

@RestController
public class SampleController {

	@Autowired
	private SampleService sampleService;

	@GetMapping("/sample")
	public ResponseEntity<List<SampleDto>> getSamples() {
		return ResponseEntity.ok()
			.body(sampleService.getSamples());
	}
}
