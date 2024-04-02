package com.walder.at.lib.client;

import com.walder.at.lib.data.JsonRequestResolver;
import com.walder.at.lib.enumerations.HttpOperation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
class WebTargetRequestVo {
	private final WebTarget webTarget;
	private final JsonRequestResolver requestResolver;
	private final HttpOperation httpOperation;
	private final Response.StatusType expectedHttpStatus;
	@Builder.Default
	private final LocalDateTime startTime = LocalDateTime.now();
}
