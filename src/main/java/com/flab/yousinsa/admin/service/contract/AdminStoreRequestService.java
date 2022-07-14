package com.flab.yousinsa.admin.service.contract;

import com.flab.yousinsa.admin.domain.dtos.RequestStoreDtoRequest;
import com.flab.yousinsa.admin.domain.dtos.RequestStoreDtoResponse;

public interface AdminStoreRequestService {
	RequestStoreDtoResponse acceptStoreRequest(RequestStoreDtoRequest requestStoreDtoRequest);
}
