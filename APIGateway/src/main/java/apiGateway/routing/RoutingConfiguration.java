package apiGateway.routing;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ServerWebExchange;

@Configuration
public class RoutingConfiguration {

	@Bean
	RouteLocator gatewayRouting(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p.path("/currency-exchange").uri("lb://currency-exchange"))
				.route(p -> p.path("/crypto-exchange").uri("lb://crypto-exchange"))
				.route(p -> p.path("/currency-conversion-feign")
						.filters(f -> f.filter((exchange, chain) -> {
							return exchange.getPrincipal()
									.flatMap(principal -> {
										if (principal instanceof Authentication) {
											Authentication auth = (Authentication) principal;
											String username = auth.getName();
											String role = auth.getAuthorities().stream()
													.findFirst()
													.map(GrantedAuthority::getAuthority)
													.map(r -> r.replace("ROLE_", ""))
													.orElse("USER");

											ServerWebExchange modifiedExchange = exchange.mutate()
													.request(exchange.getRequest().mutate()
															.header("X-User-Email", username)
															.header("X-User-Role", role)
															.build())
													.build();

											return chain.filter(modifiedExchange);
										}
										return chain.filter(exchange);
									})
									.switchIfEmpty(chain.filter(exchange));
						}))
						.uri("lb://currency-conversion"))
				.route(p -> p.path("/currency-conversion")
						.filters(f -> f.filter((exchange, chain) -> {
							return exchange.getPrincipal()
									.flatMap(principal -> {
										if (principal instanceof Authentication) {
											Authentication auth = (Authentication) principal;
											String username = auth.getName();
											String role = auth.getAuthorities().stream()
													.findFirst()
													.map(GrantedAuthority::getAuthority)
													.map(r -> r.replace("ROLE_", ""))
													.orElse("USER");

											ServerWebExchange modifiedExchange = exchange.mutate()
													.request(exchange.getRequest().mutate()
															.header("X-User-Email", username)
															.header("X-User-Role", role)
															.build())
													.build();

											return chain.filter(modifiedExchange);
										}
										return chain.filter(exchange);
									})
									.switchIfEmpty(chain.filter(exchange));
						}).rewritePath("/currency-conversion", "/currency-conversion-feign"))
						.uri("lb://currency-conversion"))
				.route(p -> p.path("/crypto-conversion-feign")
						.filters(f -> f.filter((exchange, chain) -> {
							return exchange.getPrincipal()
									.flatMap(principal -> {
										if (principal instanceof Authentication) {
											Authentication auth = (Authentication) principal;
											String username = auth.getName();
											String role = auth.getAuthorities().stream()
													.findFirst()
													.map(GrantedAuthority::getAuthority)
													.map(r -> r.replace("ROLE_", ""))
													.orElse("USER");

											ServerWebExchange modifiedExchange = exchange.mutate()
													.request(exchange.getRequest().mutate()
															.header("X-User-Email", username)
															.header("X-User-Role", role)
															.build())
													.build();

											return chain.filter(modifiedExchange);
										}
										return chain.filter(exchange);
									})
									.switchIfEmpty(chain.filter(exchange));
						}))
						.uri("lb://crypto-conversion"))
				.route(p -> p.path("/crypto-conversion")
						.filters(f -> f.filter((exchange, chain) -> {
							return exchange.getPrincipal()
									.flatMap(principal -> {
										if (principal instanceof Authentication) {
											Authentication auth = (Authentication) principal;
											String username = auth.getName();
											String role = auth.getAuthorities().stream()
													.findFirst()
													.map(GrantedAuthority::getAuthority)
													.map(r -> r.replace("ROLE_", ""))
													.orElse("USER");

											ServerWebExchange modifiedExchange = exchange.mutate()
													.request(exchange.getRequest().mutate()
															.header("X-User-Email", username)
															.header("X-User-Role", role)
															.build())
													.build();

											return chain.filter(modifiedExchange);
										}
										return chain.filter(exchange);
									})
									.switchIfEmpty(chain.filter(exchange));
						}).rewritePath("/crypto-conversion", "/crypto-conversion-feign"))
						.uri("lb://crypto-conversion"))
				.route(p -> p.path("/users/**")
						.filters(f -> f.filter((exchange, chain) -> {
							return exchange.getPrincipal()
									.flatMap(principal -> {
										if (principal instanceof Authentication) {
											Authentication auth = (Authentication) principal;
											String username = auth.getName();
											String role = auth.getAuthorities().stream()
													.findFirst()
													.map(GrantedAuthority::getAuthority)
													.map(r -> r.replace("ROLE_", ""))
													.orElse("USER");

											ServerWebExchange modifiedExchange = exchange.mutate()
													.request(exchange.getRequest().mutate()
															.header("X-User-Email", username)
															.header("X-User-Role", role)
															.build())
													.build();

											return chain.filter(modifiedExchange);
										}
										return chain.filter(exchange);
									})
									.switchIfEmpty(chain.filter(exchange));
						}))
						.uri("lb://users-service"))
				.route(p -> p.path("/bank-account/**")
						.filters(f -> f.filter((exchange, chain) -> {
							return exchange.getPrincipal()
									.flatMap(principal -> {
										if (principal instanceof Authentication) {
											Authentication auth = (Authentication) principal;
											String username = auth.getName();
											String role = auth.getAuthorities().stream()
													.findFirst()
													.map(GrantedAuthority::getAuthority)
													.map(r -> r.replace("ROLE_", ""))
													.orElse("USER");

											ServerWebExchange modifiedExchange = exchange.mutate()
													.request(exchange.getRequest().mutate()
															.header("X-User-Email", username)
															.header("X-User-Role", role)
															.build())
													.build();

											return chain.filter(modifiedExchange);
										}
										return chain.filter(exchange);
									})
									.switchIfEmpty(chain.filter(exchange));
						}))
						.uri("lb://bank-account"))
				.route(p -> p.path("/crypto-wallet/**")
						.filters(f -> f.filter((exchange, chain) -> {
							return exchange.getPrincipal()
									.flatMap(principal -> {
										if (principal instanceof Authentication) {
											Authentication auth = (Authentication) principal;
											String username = auth.getName();
											String role = auth.getAuthorities().stream()
													.findFirst()
													.map(GrantedAuthority::getAuthority)
													.map(r -> r.replace("ROLE_", ""))
													.orElse("USER");

											ServerWebExchange modifiedExchange = exchange.mutate()
													.request(exchange.getRequest().mutate()
															.header("X-User-Email", username)
															.header("X-User-Role", role)
															.build())
													.build();

											return chain.filter(modifiedExchange);
										}
										return chain.filter(exchange);
									})
									.switchIfEmpty(chain.filter(exchange));
						}))
						.uri("lb://crypto-wallet"))
				.route(p -> p.path("/trade-service")
						.filters(f -> f.filter((exchange, chain) -> {
							return exchange.getPrincipal()
									.flatMap(principal -> {
										if (principal instanceof Authentication) {
											Authentication auth = (Authentication) principal;
											String username = auth.getName();
											String role = auth.getAuthorities().stream()
													.findFirst()
													.map(GrantedAuthority::getAuthority)
													.map(r -> r.replace("ROLE_", ""))
													.orElse("USER");

											ServerWebExchange modifiedExchange = exchange.mutate()
													.request(exchange.getRequest().mutate()
															.header("X-User-Email", username)
															.header("X-User-Role", role)
															.build())
													.build();

											return chain.filter(modifiedExchange);
										}
										return chain.filter(exchange);
									})
									.switchIfEmpty(chain.filter(exchange));
						}))
						.uri("lb://trade-service"))
				.build();
	}
}
