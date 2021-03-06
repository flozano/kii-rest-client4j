package com.kii.cloud.rest.client.resource;

import java.util.Map;

import com.kii.cloud.rest.client.model.KiiCredentialsContainer;
import com.kii.cloud.rest.client.model.push.KiiPushInstallation.InstallationType;
import com.kii.cloud.rest.client.model.storage.KiiGroup;
import com.kii.cloud.rest.client.model.storage.KiiThing;
import com.kii.cloud.rest.client.model.storage.KiiUser;
import com.kii.cloud.rest.client.resource.analytics.KiiAggregationRuleResource;
import com.kii.cloud.rest.client.resource.analytics.KiiAggregationRulesResource;
import com.kii.cloud.rest.client.resource.analytics.KiiAnalyticsResource;
import com.kii.cloud.rest.client.resource.analytics.KiiConversionRuleResource;
import com.kii.cloud.rest.client.resource.analytics.KiiConversionRulesResource;
import com.kii.cloud.rest.client.resource.analytics.KiiEventsResource;
import com.kii.cloud.rest.client.resource.conf.KiiAppConfigurationResource;
import com.kii.cloud.rest.client.resource.push.KiiPushInstallationResource;
import com.kii.cloud.rest.client.resource.push.KiiPushInstallationsResource;
import com.kii.cloud.rest.client.resource.push.KiiTopicResource;
import com.kii.cloud.rest.client.resource.push.KiiTopicsResource;
import com.kii.cloud.rest.client.resource.servercode.KiiServerCodeHookResource;
import com.kii.cloud.rest.client.resource.servercode.KiiServerCodeHooksResource;
import com.kii.cloud.rest.client.resource.servercode.KiiServerCodeResource;
import com.kii.cloud.rest.client.resource.servercode.KiiServerCodesResource;
import com.kii.cloud.rest.client.resource.social.SocialIntegrationResource;
import com.kii.cloud.rest.client.resource.storage.KiiBucketResource;
import com.kii.cloud.rest.client.resource.storage.KiiEncryptedBucketResource;
import com.kii.cloud.rest.client.resource.storage.KiiGroupResource;
import com.kii.cloud.rest.client.resource.storage.KiiGroupsResource;
import com.kii.cloud.rest.client.resource.storage.KiiScopeAclResource;
import com.kii.cloud.rest.client.resource.storage.KiiThingResource;
import com.kii.cloud.rest.client.resource.storage.KiiThingsResource;
import com.kii.cloud.rest.client.resource.storage.KiiUserResource;
import com.kii.cloud.rest.client.resource.storage.KiiUsersResource;

/**
 * Represents the application resource like following URI:
 * <ul>
 * <li>https://hostname/api/apps/{APP_ID}
 * </ul>
 * <p>
 * This resource is the root for all other resources.
 */
public class KiiAppResource extends KiiRestResource {
	public static final String BASE_PATH = "/apps";
	
	private final String appID;
	private final String appKey;
	private final String endpoint;
	private final KiiCredentialsContainer credentials;
	
	public KiiAppResource(String appID, String appKey, String endpoint, KiiCredentialsContainer credentials) {
		this.appID = appID;
		this.appKey = appKey;
		this.endpoint = endpoint;
		this.credentials = credentials;
	}
	
	public String getAppID() {
		return this.appID;
	}
	public String getAppKey() {
		return this.appKey;
	}
	public String getEndpoint() {
		return this.endpoint;
	}
	public KiiCredentialsContainer getCredentials() {
		return this.credentials;
	}

	public KiiOAuthResource oauth() {
		return new KiiOAuthResource(this);
	}
	public KiiAppConfigurationResource configuration() {
		return new KiiAppConfigurationResource(this);
	}
	public KiiUsersResource users() {
		return new KiiUsersResource(this);
	}
	public KiiUserResource users(String identifier) {
		return new KiiUserResource(this.users(), identifier);
	}
	public KiiUserResource users(KiiUser user) {
		return new KiiUserResource(this.users(), user.getUserID());
	}
	public KiiThingsResource things() {
		return new KiiThingsResource(this);
	}
	public KiiThingResource things(KiiThing thing) {
		return new KiiThingResource(this.things(), thing.getIdentifier());
	}
	public KiiThingResource things(String identifier) {
		return new KiiThingResource(this.things(), identifier);
	}
	public KiiGroupsResource groups() {
		return new KiiGroupsResource(this);
	}
	public KiiGroupResource groups(KiiGroup group) {
		return groups(group.getGroupID());
	}
	public KiiGroupResource groups(String groupID) {
		return new KiiGroupResource(this.groups(), groupID);
	}
	public KiiBucketResource buckets(String name) {
		return new KiiBucketResource(this, name);
	}
	public KiiEncryptedBucketResource encryptedBuckets(String name) {
		return new KiiEncryptedBucketResource(this, name);
	}
	public KiiTopicsResource topics() {
		return new KiiTopicsResource(this);
	}
	public KiiTopicResource topics(String name) {
		return new KiiTopicResource(this.topics(), name);
	}
	public KiiScopeAclResource acl() {
		return new KiiScopeAclResource(this);
	}
	public KiiPushInstallationsResource installations() {
		return new KiiPushInstallationsResource(this);
	}
	public KiiPushInstallationResource installations(String installationID) {
		return new KiiPushInstallationResource(this.installations(), installationID);
	}
	public KiiPushInstallationResource installations(InstallationType installationType, String installationRegistrationID) {
		return new KiiPushInstallationResource(this.installations(), installationType, installationRegistrationID);
	}
	public KiiServerCodesResource servercode() {
		return new KiiServerCodesResource(this);
	}
	public KiiServerCodeResource servercode(String version) {
		return new KiiServerCodeResource(servercode(), version);
	}
	public KiiServerCodeHooksResource hooks() {
		return new KiiServerCodeHooksResource(this);
	}
	public KiiServerCodeHookResource hooks(String version) {
		return new KiiServerCodeHookResource(this.hooks(), version);
	}
	
	public KiiEventsResource events() {
		return new KiiEventsResource(this);
	}
	public KiiAnalyticsResource analytics(String aggregationRuleID) {
		return new KiiAnalyticsResource(this, aggregationRuleID);
	}
	public KiiConversionRulesResource conversionRules() {
		return new KiiConversionRulesResource(this);
	}
	public KiiConversionRuleResource conversionRules(String conversionRuleID) {
		return new KiiConversionRuleResource(this.conversionRules(), conversionRuleID);
	}
	public KiiAggregationRulesResource aggregationRules() {
		return new KiiAggregationRulesResource(this);
	}
	public KiiAggregationRuleResource aggregationRules(String aggregationRuleID) {
		return new KiiAggregationRuleResource(this.aggregationRules(), aggregationRuleID);
	}
	public SocialIntegrationResource social() {
		return new SocialIntegrationResource(this);
	}
	
	@Override
	public String getPath() {
		return this.endpoint + BASE_PATH + "/" + this.appID;
	}
	@Override
	protected KiiRestResource getParent() {
		// This class is root resource
		return null;
	}
	@Override
	protected void setAppHeader(Map<String, String> headers) {
		headers.put("X-Kii-AppID", this.appID);
		headers.put("X-Kii-AppKey", this.appKey);
	}
	@Override
	protected void setAuthorizationHeader(Map<String, String> headers) {
		if (this.credentials != null) {
			headers.put("Authorization", "Bearer " + this.credentials.getAccessToken());
		}
	}
}
