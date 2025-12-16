//forwarding engine
//
//
//
package com.siva.main.gw;

import org.springframework.core.io.buffer.DataBuffer;
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

		return webClient
				.method(req.getMethod())
				.uri(targetUrl)
				.headers(h -> HeaderUtil.copyRequestHeaders(req.getHeaders(), h))
				.body(req.getBody(), DataBuffer.class)
				.exchangeToMono(clientResponse -> {
					exchange.getResponse().setStatusCode(clientResponse.statusCode());
					return exchange.getResponse()
							.writeWith(clientResponse.bodyToFlux(DataBuffer.class));
				});
	}

	// public Mono<Void> forward(ServerWebExchange exchange, RouteDef route) {
	// ServerHttpRequest req = exchange.getRequest();
	// String targetUrl = PathRewriteUtil.buildTargetUrl(req, route);
	//
	// return webClient
	// .method(req.getMethod())
	// .uri(targetUrl)
	// .headers(h -> HeaderUtil.copyRequestHeaders(req.getHeaders(), h))
	// .body(exchange.getRequest().getBody(), DataBuffer.class) // also check with
	// bytes[].class
	// .exchangeToMono(clientResponse -> {
	// exchange.getResponse().setStatusCode(clientResponse.statusCode());
	// HeaderUtil.copyResponseHeaders(clientResponse.headers().asHttpHeaders(),
	// exchange.getResponse().getHeaders());
	// return exchange.getResponse()
	// .writeWith(clientResponse.bodyToFlux(DataBuffer.class));
	// });
	//
	// }

}
