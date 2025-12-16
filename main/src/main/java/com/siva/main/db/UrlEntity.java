package com.siva.main.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "url")
public class UrlEntity {

	// important cols
	@Id
	@Column("id")
	private Integer id;

	@Column("url_pattern")
	private String urlPattern;

	@Column("http_method")
	private String httpMethod;

	@Column("target_url")
	private String targetUrl;

	@Column("auth_required")
	private boolean authRequired;

	@Column("role")
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
