package com.siva.main.gw;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
// import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.siva.main.rate.RateService;

import reactor.core.publisher.Mono;

@Component
public class GatewayController {
	// rate limit import
	private final RateService rateService;
	//

	private final RouteRegistry routeRegistry;
	private final AuthFilter authFilter;
	private final ProxyService proxyService;

	// contructor
	public GatewayController(
			RouteRegistry routeRegistry,
			AuthFilter authFilter,
			ProxyService proxyService,
			RateService rateService) {
		this.routeRegistry = routeRegistry;
		this.authFilter = authFilter;
		this.proxyService = proxyService;
		this.rateService = rateService;

	}

	public Mono<Void> handle(ServerWebExchange exchange) {
		ServerHttpRequest req = exchange.getRequest();
		// ServerHttpResponse res = exchange.getResponse(); // write about response
		// later

		String clientId = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
		String path = req.getURI().getPath();
		String method = req.getMethod().name();

		if (!rateService.allowToken(clientId)) {
			ServerHttpResponse res = exchange.getResponse();
			res.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
			Mono<Void> complete = res.setComplete();
			return complete;

		}
		return routeRegistry.findMatch(path, method)
				.switchIfEmpty(Mono.error(new RuntimeException("No route found")))
				.flatMap(route -> authFilter.filter(exchange, route))
				.flatMap(route -> proxyService.forward(exchange, route));
	}
}
