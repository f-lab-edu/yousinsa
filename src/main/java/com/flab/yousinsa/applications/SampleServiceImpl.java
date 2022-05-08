package com.flab.yousinsa.applications;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flab.yousinsa.domains.SampleRepository;
import com.flab.yousinsa.interfaces.dto.SampleDto;

@Service
public class SampleServiceImpl implements SampleService {

	@Autowired
	private SampleRepository sampleRepository;

	@Override
	public List<SampleDto> getSamples() {
		return sampleRepository.getSamples();
	}
}
