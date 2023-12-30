package fr.instalite;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.instalite.authentication.AuthenticationRequest;
import fr.instalite.authentication.AuthenticationService;
import fr.instalite.authentication.RegisterRequest;
import fr.instalite.token.TokenService;
import fr.instalite.user.ERole;

@SpringBootApplication
public class InstaliteApplication {

	private static final Logger logger = LoggerFactory.getLogger(InstaliteApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(InstaliteApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService service, TokenService tokenService) {
		return args -> {
			final RegisterRequest adminRegisterRequest = RegisterRequest
					.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@mail.com")
					.password("Admin123456$")
					.role(ERole.ADMIN.name())
					.build();
			final RegisterRequest userRegisterRequest = RegisterRequest
					.builder()
					.firstname("User")
					.lastname("User")
					.email("user@mail.com")
					.password("User123456$")
					.role(ERole.USER.name())
					.build();
			final AuthenticationRequest adminLoginRequest = AuthenticationRequest
					.builder()
					.email(adminRegisterRequest.getEmail())
					.password(adminRegisterRequest.getPassword())
					.build();
			final AuthenticationRequest userLoginRequest = AuthenticationRequest
					.builder()
					.email(userRegisterRequest.getEmail())
					.password(userRegisterRequest.getPassword())
					.build();
			final String adminRegisterToken = service.register(adminRegisterRequest).getAccessToken();
			final String userRegisterToken = service.register(userRegisterRequest).getAccessToken();
			tokenService.deleteAllTokens();
			final String adminLoginToken = service.authenticate(adminLoginRequest).getAccessToken();
			final String userLoginToken = service.authenticate(userLoginRequest).getAccessToken();
			System.out.println("[Register][ADMIN] - Token (Deleted with tokenService.deleteAllTokens()) : \n" + adminRegisterToken);
			System.out.println("[Register][User] - Token (Deleted with tokenService.deleteAllTokens()) : \n" + userRegisterToken);
			System.out.println("[Login][ADMIN] - Token : " + adminLoginToken);
			System.out.println("[Login][User] - Token : " + userLoginToken);
			logger.info("[Register][ADMIN] - Token (Deleted with tokenService.deleteAllTokens()) : {}", adminRegisterToken);
			logger.info("[Register][User] - Token (Deleted with tokenService.deleteAllTokens()) : {}", userRegisterToken);
			logger.info("[Login][ADMIN] - Token : {}", adminLoginToken);
			logger.info("[Login][User] - Token : {}", userLoginToken);
		};
	}
}
