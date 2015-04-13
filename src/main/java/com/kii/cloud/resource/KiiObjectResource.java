package com.kii.cloud.resource;

import java.io.IOException;
import java.util.Map;

import com.google.gson.JsonObject;
import com.kii.cloud.KiiRestException;
import com.kii.cloud.model.storage.KiiObject;
import com.kii.cloud.resource.KiiRestRequest.Method;
import com.squareup.okhttp.Response;

public class KiiObjectResource extends KiiRestSubResource {
	
	private final String objectID;
	
	public KiiObjectResource(KiiBucketResource parent, String objectID) {
		super(parent);
		this.objectID = objectID;
	}
	public KiiObjectBodyResource body() {
		return new KiiObjectBodyResource(this);
	}
	public KiiObjectAclResource acl() {
		return new KiiObjectAclResource(this);
	}
	/**
	 * NOTE:This feature has not documented yet.
	 * @return
	 * @throws KiiRestException
	 */
	public boolean exists() throws KiiRestException {
		Map<String, String> headers = this.newAuthorizedHeaders();
		KiiRestRequest request = new KiiRestRequest(getUrl(), Method.HEAD, headers);
		try {
			Response response = this.execute(request);
			return response.isSuccessful();
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	/**
	 * @return
	 * @throws KiiRestException
	 * @see http://documentation.kii.com/en/guides/rest/managing-data/object-storages/retrieving/
	 */
	public KiiObject get() throws KiiRestException {
		Map<String, String> headers = this.newAuthorizedHeaders();
		KiiRestRequest request = new KiiRestRequest(getUrl(), Method.GET, headers);
		try {
			Response response = this.execute(request);
			JsonObject responseBody = this.parseResponseAsJsonObject(request, response);
			return new KiiObject(responseBody);
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	/**
	 * @throws KiiRestException
	 * @see http://documentation.kii.com/en/guides/rest/managing-data/object-storages/deleting/
	 */
	public void delete() throws KiiRestException {
		Map<String, String> headers = this.newAuthorizedHeaders();
		KiiRestRequest request = new KiiRestRequest(getUrl(), Method.DELETE, headers);
		try {
			Response response = this.execute(request);
			this.parseResponse(request, response);
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	/**
	 * @param object
	 * @throws KiiRestException
	 * @see http://documentation.kii.com/en/guides/rest/managing-data/object-storages/updating/
	 */
	public void update(KiiObject object) throws KiiRestException {
		// TODO:ContentType?
		Map<String, String> headers = this.newAuthorizedHeaders();
		KiiRestRequest request = new KiiRestRequest(getUrl(), Method.PUT, headers, null, object.getJsonObject());
		try {
			Response response = this.execute(request);
			String version = response.header("ETag");
			JsonObject responseBody = this.parseResponseAsJsonObject(request, response);
			Long modifiedAt = KiiObject.PROPERTY_MODIFIED_AT.get(responseBody);
			object.setModifiedAt(modifiedAt).setVersion(version);
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	/**
	 * @param object
	 * @throws KiiRestException
	 * @see http://documentation.kii.com/en/guides/rest/managing-data/object-storages/updating/
	 */
	public void updateWithOptimisticLock(KiiObject object) throws KiiRestException {
		Map<String, String> headers = this.newAuthorizedHeaders();
		headers.put("If-Match", object.getVersion());
		KiiRestRequest request = new KiiRestRequest(getUrl(), Method.PUT, headers, null, object.getJsonObject());
		try {
			Response response = this.execute(request);
			String version = response.header("ETag");
			JsonObject responseBody = this.parseResponseAsJsonObject(request, response);
			Long modifiedAt = KiiObject.PROPERTY_MODIFIED_AT.get(responseBody);
			object.setModifiedAt(modifiedAt).setVersion(version);
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	/**
	 * @param object
	 * @throws KiiRestException
	 * @see http://documentation.kii.com/en/guides/rest/managing-data/object-storages/updating/
	 */
	public void partialUpdate(KiiObject object) throws KiiRestException {
		Map<String, String> headers = this.newAuthorizedHeaders();
		headers.put("X-HTTP-Method-Override", "PATCH");
		KiiRestRequest request = new KiiRestRequest(getUrl(), Method.POST, headers, null, object.getJsonObject());
		try {
			Response response = this.execute(request);
			JsonObject responseBody = this.parseResponseAsJsonObject(request, response);
			object.setJsonObject(responseBody);
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	/**
	 * @param object
	 * @throws KiiRestException
	 * @see http://documentation.kii.com/en/guides/rest/managing-data/object-storages/updating/
	 */
	public void partialUpdateWithOptimisticLock(KiiObject object) throws KiiRestException {
		Map<String, String> headers = this.newAuthorizedHeaders();
		headers.put("X-HTTP-Method-Override", "PATCH");
		headers.put("If-Match", object.getVersion());
		KiiRestRequest request = new KiiRestRequest(getUrl(), Method.POST, headers, null, object.getJsonObject());
		try {
			Response response = this.execute(request);
			JsonObject responseBody = this.parseResponseAsJsonObject(request, response);
			object.setJsonObject(responseBody);
		} catch (IOException e) {
			throw new KiiRestException(request.getCurl(), e);
		}
	}
	@Override
	public String getPath() {
		return KiiObjectsResource.BASE_PATH + "/" + this.objectID;
	}
}
