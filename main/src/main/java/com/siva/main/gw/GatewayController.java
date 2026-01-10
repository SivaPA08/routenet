package com.siva.main.gw;

import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GatewayController {

	private final RouteRegistry routeRegistry;
	private final AuthFilter authFilter;
	private final ProxyService proxyService;

	public GatewayController(RouteRegistry routeRegistry,
			AuthFilter authFilter,
			ProxyService proxyService) {
		this.routeRegistry = routeRegistry;
		this.authFilter = authFilter;
		this.proxyService = proxyService;
	}

	public Mono<Void> handle(ServerWebExchange exchange) {
		ServerHttpRequest req = exchange.getRequest();
		// ServerHttpResponse res = exchange.getResponse(); // write about response
		// later

		String path = req.getURI().getPath();
		String method = req.getMethod().name();

		return routeRegistry.findMatch(path, method)
				.switchIfEmpty(Mono.error(new RuntimeException("No route found")))
				.flatMap(route -> authFilter.filter(exchange, route))
				.flatMap(route -> proxyService.forward(exchange, route));
	}
}
