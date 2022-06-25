package com.flab.yousinsa;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

/**
 * Spring Rest Docs Util
 *
 * REF
 * - https://techblog.woowahan.com/2597/
 */
public interface ApiDocumentUtils {

	static OperationRequestPreprocessor getDocumentRequest() {
		return preprocessRequest(
			modifyUris()
				.scheme("http")
				.host("115.85.183.44")
				.removePort(),
			prettyPrint()
		);
	}

	static OperationResponsePreprocessor getDocumentResponse() {
		return preprocessResponse(prettyPrint());
	}
}
