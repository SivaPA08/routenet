//forwarding engine
//
//
//
package com.siva.main.gw;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Service
public class ProxyService {

	private final WebClient webClient = WebClient.builder().build();

	public Mono<Void> forward(ServerWebExchange exchange, RouteDef route) {
		ServerHttpRequest req = exchange.getRequest();
		String targetUrl = PathRewriteUtil.buildTargetUrl(req, route);

		return null;

	}

}
