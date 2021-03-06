package com.kii.cloud.rest.client.resource.servercode;

import java.io.IOException;
import java.util.Map;

import com.google.gson.JsonObject;
import com.kii.cloud.rest.client.annotation.AdminAPI;
import com.kii.cloud.rest.client.exception.KiiRestException;
import com.kii.cloud.rest.client.model.servercode.KiiScheduleExecutionQuery;
import com.kii.cloud.rest.client.model.servercode.KiiScheduleExecutionQueryResult;
import com.kii.cloud.rest.client.model.servercode.KiiScheduleExecutionResult;
import com.kii.cloud.rest.client.resource.KiiRestRequest;
import com.kii.cloud.rest.client.resource.KiiRestSubResource;
import com.kii.cloud.rest.client.resource.KiiRestRequest.Method;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Response;

/**
 * Represents the execution result of server hook resource like following URI:
 * <ul>
 * <li>https://hostname/api/apps/{APP_ID}/hooks/executions
 * </ul>
 */
public class KiiServerCodeHookExecutionsResource extends KiiRestSubResource {
	
	public static final String BASE_PATH = "/executions";
	
	public static final MediaType MEDIA_TYPE_SCHEDULE_EXECUTION_QUERY_REQUEST = MediaType.parse("application/vnd.kii.ScheduleExecutionQueryRequest+json");
	
	public KiiServerCodeHookExecutionsResource(KiiServerCodeHooksResource parent) {
		super(parent);
	}
	@AdminAPI
	public KiiScheduleExecutionResult get(String scheduleExecutionID) throws KiiRestException {
		if (scheduleExecutionID == null) {
			throw new IllegalArgumentException("scheduleExecutionID is null");
		}
		Map<String, String> headers = this.newAuthorizedHeaders();
		KiiRestRequest request = new KiiRestRequest(getUrl("/" + scheduleExecutionID), Method.GET, headers);
		try {
			Response response = this.execute(request);
			JsonObject responseBody = this.parseResponseAsJsonObject(request, response);
			return new KiiScheduleExecutionResult(responseBody);
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	@AdminAPI
	public KiiScheduleExecutionQueryResult query(KiiScheduleExecutionQuery query) throws KiiRestException {
		if (query == null) {
			throw new IllegalArgumentException("query is null");
		}
		Map<String, String> headers = this.newAuthorizedHeaders();
		KiiRestRequest request = new KiiRestRequest(getUrl("/query"), Method.POST, headers, MEDIA_TYPE_SCHEDULE_EXECUTION_QUERY_REQUEST, query.toJson());
		try {
			Response response = this.execute(request);
			JsonObject responseBody = this.parseResponseAsJsonObject(request, response);
			return new KiiScheduleExecutionQueryResult(query, responseBody);
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	@Override
	public String getPath() {
		return BASE_PATH;
	}
}
