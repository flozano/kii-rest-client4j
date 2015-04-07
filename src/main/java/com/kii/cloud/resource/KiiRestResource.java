package com.kii.cloud.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okio.BufferedSink;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kii.cloud.KiiRestException;
import com.kii.cloud.OkHttpClientFactory;
import com.kii.cloud.util.IOUtils;
import com.kii.cloud.util.Path;
import com.kii.cloud.util.StringUtils;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public abstract class KiiRestResource {
	
	public static final MediaType MEDIA_TYPE_APPLICATION_JSON = MediaType.parse("application/json");
	public static final MediaType MEDIA_TYPE_TEXT_PLAIN = MediaType.parse("text/plain");
	
	protected static final OkHttpClient client = OkHttpClientFactory.newInstance();
	
	protected abstract KiiRestResource getParent();
	public abstract String getPath();
	
	protected KiiAppResource getRootResource() {
		KiiRestResource parent = this;
		while (parent.getParent() != null) {
			parent = parent.getParent();
		}
		return (KiiAppResource)parent;
	}
	public String getUrl() {
		return this.getUrl(null);
	}
	public String getUrl(String path) {
		return this.buildUrl(path);
	}
	private String buildUrl(String path) {
		String url = buildUrl("", this);
		return Path.combine(url, path);
	}
	private String buildUrl(String path, KiiRestResource resource) {
		if (resource.getParent() != null) {
			path = buildUrl(path, resource.getParent());
		}
		return Path.combine(path, resource.getPath());
	}
	/**
	 * Create http headers that is set X-Kii-AppID and X-Kii-AppKey.
	 * 
	 * @return
	 */
	protected Map<String, String> newAppHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		this.setAppHeader(headers);
		return headers;
	}
	/**
	 * Create http headers that is set X-Kii-AppID and X-Kii-AppKey and Authorization.
	 * 
	 * @return
	 */
	protected Map<String, String> newAuthorizedHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		this.setAppHeader(headers);
		this.setAuthorizationHeader(headers);
		return headers;
	}
	protected void setAppHeader(Map<String, String> headers) {
		if (getParent() != null) {
			getParent().setAppHeader(headers);
		}
	}
	protected void setAuthorizationHeader(Map<String, String> headers) {
		if (getParent() != null) {
			getParent().setAuthorizationHeader(headers);
		}
	}
	protected void parseResponse(KiiRestRequest request, Response response) throws KiiRestException {
		try {
			String body = response.body().string();
			if (!response.isSuccessful()) {
				JsonObject errorDetail = null;
				try {
					errorDetail = (JsonObject)new JsonParser().parse(body);
				} catch (Exception ignore) {
				}
				System.out.println(request.getCurl() + "  : " + response.code());
				System.out.println(body);
				throw new KiiRestException(request.getCurl(), response.code(), errorDetail);
			}
			System.out.println(request.getCurl() + "  : " + response.code());
			System.out.println(body);
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	protected JsonObject parseResponseAsJsonObject(KiiRestRequest request, Response response) throws KiiRestException {
		try {
			String body = response.body().string();
			if (!response.isSuccessful()) {
				JsonObject errorDetail = null;
				try {
					errorDetail = (JsonObject)new JsonParser().parse(body);
				} catch (Exception ignore) {
				}
				System.out.println(request.getCurl() + "  : " + response.code());
				System.out.println(body);
				throw new KiiRestException(request.getCurl(), response.code(), errorDetail);
			}
			System.out.println(request.getCurl() + "  : " + response.code());
			System.out.println(body);
			if (StringUtils.isEmpty(body)) {
				return null;
			}
			return (JsonObject)new JsonParser().parse(body);
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	protected InputStream parseResponseAsInputStream(KiiRestRequest request, Response response) throws KiiRestException {
		try {
			if (!response.isSuccessful()) {
				String body = response.body().string();
				JsonObject errorDetail = null;
				try {
					errorDetail = (JsonObject)new JsonParser().parse(body);
				} catch (Exception ignore) {
				}
				System.out.println(request.getCurl() + "  : " + response.code());
				System.out.println(body);
				throw new KiiRestException(request.getCurl(), response.code(), errorDetail);
			}
			System.out.println(request.getCurl() + "  : " + response.code());
			return response.body().byteStream();
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	protected Response execute(KiiRestRequest restRequest) throws IOException {
		Builder bulder = new Request.Builder();
		bulder.url(restRequest.getUrl());
		bulder.headers(Headers.of(restRequest.getHeaders()));
		OkHttpClient httpClient = client;
		switch (restRequest.getMethod()) {
			case HEAD:
				bulder.head();
				break;
			case GET:
				bulder.get();
				break;
			case POST:
				bulder.post(this.createRequestBody(restRequest.getContentType(), restRequest.getEntity()));
				httpClient = httpClient.clone();
				httpClient.setRetryOnConnectionFailure(false);
				break;
			case PUT:
				bulder.put(this.createRequestBody(restRequest.getContentType(), restRequest.getEntity()));
				httpClient = httpClient.clone();
				httpClient.setRetryOnConnectionFailure(false);
				break;
			case DELETE:
				bulder.delete();
				break;
		}
		Request request = bulder.build();
		return httpClient.newCall(request).execute();
	}
	protected RequestBody createRequestBody(final MediaType contentType, final Object entity) {
		if (entity == null) {
			return RequestBody.create(contentType, "");
		}
		if (entity instanceof String) {
			return RequestBody.create(contentType, (String)entity);
		}
		if (entity instanceof JsonObject) {
			return RequestBody.create(contentType, ((JsonObject)entity).toString());
		}
		if (entity instanceof InputStream) {
			new RequestBody() {
				@Override
				public MediaType contentType() {
					return contentType;
				}
				@Override
				public void writeTo(BufferedSink sink) throws IOException {
					OutputStream os = sink.outputStream();
					IOUtils.copy((InputStream)entity, os);
				}
			};
		}
		throw new RuntimeException("Unexpected entity type.");
	}
}
