package com.siva.main.gw;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import org.springframework.core.io.buffer.DataBuffer;

@Component
public class GatewayFilter implements WebFilter {

	private static final Logger log = LoggerFactory.getLogger(GatewayFilter.class);

	private final GatewayController gatewayController;

	public GatewayFilter(GatewayController gatewayController) {
		this.gatewayController = gatewayController;
	}

	private boolean goToWebPage(String path) {
		return path.startsWith("/admin");
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		// We handle everything in gatewayController. Do NOT call
		// chain.filter(exchange).
		if (goToWebPage(exchange.getRequest().getURI().getPath())) {
			return chain.filter(exchange);
		}
		return gatewayController.handle(exchange)
				.onErrorResume(e -> handleError(exchange, e));
	}

	private Mono<Void> handleError(ServerWebExchange exchange, Throwable e) {
		ServerHttpResponse res = exchange.getResponse();

		// If response already committed, re-throw so container can log / handle it.
		if (res.isCommitted()) {
			log.warn("Response already committed while handling error: {}", e.getMessage(), e);
			return Mono.error(e);
		}

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		// Select status by exception type

		// add exception here
		res.setStatusCode(status);
		res.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		String msg = e.getMessage() != null ? e.getMessage() : status.getReasonPhrase();
		String json = "{\"error\":\"" + escapeJson(msg) + "\"}";
		byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = res.bufferFactory().wrap(bytes);
		res.getHeaders().setContentLength(bytes.length);

		return res.writeWith(Mono.just(buffer));
	}

	// Very small JSON escape utility — enough for short error messages
	private String escapeJson(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder(s.length() + 8);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
				case '"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				default:
					if (c < 0x20) {
						sb.append(String.format("\\u%04x", (int) c));
					} else {
						sb.append(c);
					}
			}
		}
		return sb.toString();
	}
}
