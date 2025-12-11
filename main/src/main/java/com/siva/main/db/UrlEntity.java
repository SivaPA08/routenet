package com.siva.main.db;

import jakarta.persistence.Entity;

@Entity
// @Table(name = "url")
public class UrlEntity {

	// important cols
	private Integer id;
	private String urlPattern;
	private String httpMethod;
	private String targetUrl; // address of the url
	private boolean authRequired;
	private String role;

	// imp cols ends
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public boolean isAuthRequired() {
		return authRequired;
	}

	public void setAuthRequired(boolean authRequired) {
		this.authRequired = authRequired;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
