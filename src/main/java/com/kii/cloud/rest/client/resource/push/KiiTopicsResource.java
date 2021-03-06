package com.kii.cloud.rest.client.resource.push;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kii.cloud.rest.client.exception.KiiRestException;
import com.kii.cloud.rest.client.resource.KiiAppResource;
import com.kii.cloud.rest.client.resource.KiiRestRequest;
import com.kii.cloud.rest.client.resource.KiiRestSubResource;
import com.kii.cloud.rest.client.resource.KiiRestRequest.Method;
import com.kii.cloud.rest.client.resource.storage.KiiGroupResource;
import com.kii.cloud.rest.client.resource.storage.KiiThingResource;
import com.kii.cloud.rest.client.resource.storage.KiiUserResource;
import com.kii.cloud.rest.client.util.GsonUtils;
import com.squareup.okhttp.Response;

public class KiiTopicsResource extends KiiRestSubResource {
	public static final String BASE_PATH = "/topics";
	public KiiTopicsResource(KiiAppResource parent) {
		super(parent);
	}
	public KiiTopicsResource(KiiUserResource parent) {
		super(parent);
	}
	public KiiTopicsResource(KiiGroupResource parent) {
		super(parent);
	}
	public KiiTopicsResource(KiiThingResource parent) {
		super(parent);
	}
	/**
	 * Gets list of topic names.
	 * 
	 * @return
	 * @throws KiiRestException
	 */
	public List<String> list() throws KiiRestException {
		Map<String, String> headers = this.newAuthorizedHeaders();
		KiiRestRequest request = new KiiRestRequest(getUrl(), Method.GET, headers);
		try {
			Response response = this.execute(request);
			JsonObject responseBody = this.parseResponseAsJsonObject(request, response);
			List<String> result = new ArrayList<String>();
			JsonArray topics = GsonUtils.getJsonArray(responseBody, "topics");
			for (int i = 0; i < topics.size(); i++) {
				result.add(GsonUtils.getString(topics.get(i).getAsJsonObject(), "topicID"));
			}
			return result;
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	@Override
	public String getPath() {
		return BASE_PATH;
	}
}
