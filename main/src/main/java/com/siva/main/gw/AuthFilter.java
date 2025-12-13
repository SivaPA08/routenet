//here the message first enters
//
//
//
package com.siva.main.gw;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.siva.main.jwt.JwtService;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter {

	private final JwtService jwtService;

	public AuthFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	public Mono<RouteDef> filter(ServerWebExchange exchange, RouteDef route) {
		if (!route.isAuthRequired()) {
			return Mono.just(route);
		}
		String auth = exchange.getRequest().getHeaders().getFirst("Authorization");
		if (auth == null || !auth.startsWith("Bearer ")) {
			return Mono.error(new RuntimeException("Missing token bro"));
		}
		String token = auth.substring(7);
		try {
			String payload = jwtService.validateToken(token);
			if (route.getRole() != null && !payload.contains("\"role\":\"" + route.getRole() + "\"")) {
				return Mono.error(new RuntimeException("Forbidden"));
			}
			return Mono.just(route);
		} catch (Exception e) {
			return Mono.error(new RuntimeException("Invalid token bro"));

		}

	}
}
