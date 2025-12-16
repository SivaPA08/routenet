package com.siva.main.gw;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RequestPredicates;

@Configuration
public class GatewayRouter {

	private final GatewayController gatewayController;

	public GatewayRouter(GatewayController gatewayController) {
		this.gatewayController = gatewayController;
	}

	@Bean
	public RouterFunction<ServerResponse> routes() {
		return RouterFunctions.route(
				RequestPredicates.all(),
				request -> {
					gatewayController.handle(request.exchange()).subscribe();
					return ServerResponse.ok().build();
				});
	}
}
