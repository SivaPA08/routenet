package com.siva.main.gw;

import com.siva.main.db.UrlEntity;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class RouteDef {

	private static final PathPatternParser parser = new PathPatternParser();

	private final UrlEntity entity;
	private final String rawPattern;
	private final PathPattern compiledPattern;
	private final String httpMethod;
	private final String targetBase;
	private final boolean authRequired;
	private final String role;

	public RouteDef(UrlEntity e) {
		this.entity = e;
		this.rawPattern = e.getUrlPattern();
		this.compiledPattern = (rawPattern == null ? null : parser.parse(rawPattern));

		this.httpMethod = e.getHttpMethod();
		this.targetBase = e.getTargetUrl();
		this.authRequired = e.isAuthRequired();
		this.role = e.getRole();
	}

	public boolean matches(String incomingPath, String incomingMethod) {
		if (compiledPattern == null)
			return false;

		if (!compiledPattern.matches(PathContainer.parsePath(incomingPath))) {
			return false;
		}

		if (httpMethod == null || httpMethod.isBlank() || httpMethod.equalsIgnoreCase("ANY"))
			return true;

		return incomingMethod.equalsIgnoreCase(httpMethod);
	}

	public UrlEntity getEntity() {
		return entity;
	}

	public String getPattern() {
		return rawPattern;
	}

	public String getTargetBase() {
		return targetBase;
	}

	public boolean isAuthRequired() {
		return authRequired;
	}

	public String getRole() {
		return role;
	}
}
