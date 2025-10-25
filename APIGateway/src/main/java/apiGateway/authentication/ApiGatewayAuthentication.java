package apiGateway.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import api.dtos.UserDto;


@Configuration
@EnableWebFluxSecurity
public class ApiGatewayAuthentication {

	@Bean
	SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeExchange(exchange -> exchange
				.pathMatchers(HttpMethod.POST, "/users/**").hasAnyRole("OWNER", "ADMIN")
				.pathMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("OWNER", "ADMIN")
				.pathMatchers(HttpMethod.DELETE, "/users/**").hasRole("OWNER")
				.pathMatchers(HttpMethod.GET, "/users/**").hasAnyRole("OWNER", "ADMIN")
				.pathMatchers(HttpMethod.POST, "/bank-account/**").hasRole("ADMIN")
				.pathMatchers(HttpMethod.PUT, "/bank-account/**").hasRole("ADMIN")
				.pathMatchers(HttpMethod.DELETE, "/bank-account/**").hasRole("ADMIN")
				.pathMatchers(HttpMethod.GET, "/bank-account/**").hasAnyRole("ADMIN", "USER")
				.pathMatchers(HttpMethod.POST, "/crypto-wallet/**").hasRole("ADMIN")
				.pathMatchers(HttpMethod.PUT, "/crypto-wallet/**").hasRole("ADMIN")
				.pathMatchers(HttpMethod.DELETE, "/crypto-wallet/**").hasRole("ADMIN")
				.pathMatchers(HttpMethod.GET, "/crypto-wallet/**").hasAnyRole("ADMIN", "USER")
				.pathMatchers("/currency-exchange").hasAnyRole("OWNER", "ADMIN", "USER")
				.pathMatchers("/crypto-exchange").hasAnyRole("OWNER", "ADMIN", "USER")
				.pathMatchers("/currency-conversion", "/currency-conversion-feign").hasRole("USER")
				.pathMatchers("/crypto-conversion", "/crypto-conversion-feign").hasRole("USER")
				.pathMatchers("/trade-service").hasRole("USER")
				).httpBasic(Customizer.withDefaults());

		return http.build();
	}
	
	@Bean
	ReactiveUserDetailsService reactiveUserDetailsService(WebClient.Builder webClientBuilder, BCryptPasswordEncoder encoder) {
		/*Test lokalno
		 * WebClient client = webClientBuilder.baseUrl("http://localhost:8770").build();*/


		WebClient client = webClientBuilder.baseUrl("http://users-service:8770").build();

		return user -> {
			System.out.println("Attempting to authenticate user: " + user);
			return client.get()
				.uri(uriBuilder -> uriBuilder
						.path("/users/email")
						.queryParam("email", user)
						.build()
				)
				.retrieve()
				.bodyToMono(UserDto.class)
				.doOnSuccess(dto -> {
					if (dto != null) {
						System.out.println("Successfully fetched user: " + dto.getEmail() + ", role: " + dto.getRole() + ", password: " + dto.getPassword());
					} else {
						System.out.println("User DTO is null!");
					}
				})
				.doOnError(error -> System.out.println("ERROR fetching user: " + error.getMessage()))
				.map(dto -> User.withUsername(dto.getEmail())
						.password(encoder.encode(dto.getPassword()))
						.roles(dto.getRole())
						.build()
				);
		};

		}
	
	@Bean
	BCryptPasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
