package com.flab.yousinsa.domains;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.flab.yousinsa.interfaces.dto.SampleDto;

@Mapper
public interface SampleRepository {
	List<SampleDto> getSamples();
}
