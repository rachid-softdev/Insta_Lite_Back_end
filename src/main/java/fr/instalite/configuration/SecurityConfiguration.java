package fr.instalite.configuration;

import static fr.instalite.user.EPermission.ADMIN_CREATE;
import static fr.instalite.user.EPermission.ADMIN_DELETE;
import static fr.instalite.user.EPermission.ADMIN_READ;
import static fr.instalite.user.EPermission.ADMIN_UPDATE;
import static fr.instalite.user.ERole.ADMIN;
import static fr.instalite.user.ERole.NONE;
import static fr.instalite.user.ERole.USER;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String[] WHITE_LIST_URL = {
			/** Fichiers statitques */
			"/images",
			"/images/**",
			/** Authentification */
			"/api/insta_lite/authentication",
			"/api/insta_lite/authentication/**",
			"/api/check-authentication",
			"/api/check-authentication/**",
			/** Documentation */
			"/v2/api-docs",
			"/v3/api-docs",
			"/v3/api-docs/**",
			"/swagger-resources",
			"/swagger-resources/**",
			"/configuration/ui",
			"/configuration/security",
			"/swagger-ui/**",
			"/webjars/**",
			"/swagger-ui.html",
			"/instalite-api-docs/**",
			"/instalite-documentation/**",
	};

	private final JwtAuthenticationFilter jwtAuthFilter;

	private final AuthenticationProvider authenticationProvider;

	private final LogoutHandler logoutHandler;

	public SecurityConfiguration(
			@Autowired JwtAuthenticationFilter jwtAuthFilter,
			AuthenticationProvider authenticationProvider,
			LogoutHandler logoutHandler) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.authenticationProvider = authenticationProvider;
		this.logoutHandler = logoutHandler;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
			throws Exception {
		http
				// CORS :
				// https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html
				.cors(c -> corsFilter())
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(req -> req
						.requestMatchers(WHITE_LIST_URL)
						.permitAll()
						.requestMatchers(HttpMethod.GET, "/api/insta_lite/users")
						.hasAnyRole(ADMIN.name())
						.requestMatchers(HttpMethod.GET, "/api/insta_lite/users/**")
						.hasAnyRole(ADMIN.name(), USER.name())
						.requestMatchers(HttpMethod.POST, "/api/insta_lite/users")
						.hasAnyRole(ADMIN.name())
						.requestMatchers(HttpMethod.PUT, "/api/insta_lite/users")
						.hasAnyRole(ADMIN.name())
						.requestMatchers(HttpMethod.DELETE, "/api/insta_lite/users")
						.hasAnyRole(ADMIN.name())
						.requestMatchers(HttpMethod.DELETE, "/api/insta_lite/users/**")
						.hasAnyRole(ADMIN.name())
						.requestMatchers(HttpMethod.PATCH, "/api/insta_lite/users/*/profile")
						.hasAnyRole(ADMIN.name(), USER.name())
						.requestMatchers(HttpMethod.POST, "/api/insta_lite/users/*/change-password")
						.hasAnyRole(ADMIN.name(), USER.name())
						.requestMatchers(HttpMethod.GET, "/api/insta_lite/images")
						.permitAll()
						.requestMatchers(HttpMethod.GET, "/api/insta_lite/images/**")
						.permitAll()
						.requestMatchers(HttpMethod.POST, "/api/insta_lite/images")
						.hasAnyRole(ADMIN.name(), USER.name())
						.requestMatchers(HttpMethod.POST, "/api/insta_lite/images/**")
						.hasAnyRole(ADMIN.name(), USER.name())
						.requestMatchers(HttpMethod.PUT, "/api/insta_lite/images")
						.hasAnyRole(ADMIN.name())
						.requestMatchers(HttpMethod.PUT, "/api/insta_lite/images/**")
						.hasAnyRole(ADMIN.name())
						.requestMatchers(HttpMethod.DELETE, "/api/insta_lite/images")
						.hasAnyRole(ADMIN.name())
						.requestMatchers(HttpMethod.DELETE, "/api/insta_lite/images/**")
						.hasAnyRole(ADMIN.name())
						.anyRequest()
						.authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(
						jwtAuthFilter,
						UsernamePasswordAuthenticationFilter.class)
				.logout(logout -> logout
						.logoutUrl("/api/insta_lite/authentication/logout")
						.addLogoutHandler(logoutHandler)
						.logoutSuccessHandler(
								(request, response, authentication) -> SecurityContextHolder.clearContext()));

		// XSS :
		// https://stackoverflow.com/questions/76516612/headers-configuration-in-spring-security-6-for-automated-testing
		/**
		 * http.headers(headers ->
		 * headers.xssProtection(
		 * xss ->
		 * xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
		 * ).contentSecurityPolicy(
		 * cps -> cps.policyDirectives("script-src")
		 * ));
		 */
		return http.build();
	}

	@Bean
	public CorsFilter corsFilter() {
		final CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Authorization", "Origin, Accept", "X-Requested-With", "Access-Control-Request-Method",
				"Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		/**
		 * corsConfiguration.setAllowedMethods(Arrays.asList(
		 * HttpMethod.GET.name(),
		 * HttpMethod.HEAD.name(),
		 * HttpMethod.POST.name(),
		 * HttpMethod.PUT.name(),
		 * HttpMethod.PATCH.name(),
		 * HttpMethod.DELETE.name(),
		 * HttpMethod.OPTIONS.name(),
		 * HttpMethod.TRACE.name()));
		 * corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT",
		 * "PATCH", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT"));
		 */
		corsConfiguration.setMaxAge(1800L);
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}
