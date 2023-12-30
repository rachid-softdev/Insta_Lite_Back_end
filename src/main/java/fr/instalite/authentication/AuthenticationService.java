package fr.instalite.authentication;

import java.util.List;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import fr.instalite.common.RandomIDGenerator;
import fr.instalite.configuration.JwtService;
import fr.instalite.token.TokenEntity;
import fr.instalite.token.TokenService;
import fr.instalite.token.TokenType;
import fr.instalite.user.UserEntity;
import fr.instalite.user.UserMapper;
import fr.instalite.user.BaseUserResponse;
import fr.instalite.user.ERole;
import fr.instalite.user.UserService;

@Service
public class AuthenticationService {

	private final UserService userService;
	private final UserMapper userMapper;
	private final RandomIDGenerator randomIDGenerator;
	private final TokenService tokenService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationService(@Autowired UserService userService, @Autowired UserMapper userMapper,
			@Autowired RandomIDGenerator randomIDGenerator,
			@Autowired TokenService tokenService, @Autowired PasswordEncoder passwordEncoder,
			@Autowired JwtService jwtService, @Autowired AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.userMapper = userMapper;
		this.randomIDGenerator = randomIDGenerator;
		this.tokenService = tokenService;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	public AuthenticationResponse register(RegisterRequest request) {
		final UserEntity user = UserEntity.builder()
				.publicId(randomIDGenerator.generateRandomUUID())
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.email(request.getEmail())
				.password(request.getPassword())
				.role(ERole.fromValue(request.getRole()))
				.build();
		final UserEntity createdUser = userService.createUser(user);
		final String jwtToken = jwtService.generateToken(user);
		final String refreshToken = jwtService.generateRefreshToken(user);
		this.saveUserToken(createdUser, jwtToken);
		final BaseUserResponse userResponse = this.userMapper.toBaseUserResponse(createdUser);
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken)
				.userResponse(userResponse).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		this.authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		final UserEntity userEntity = userService.getUserByEmail(request.getEmail());
		final String jwtToken = jwtService.generateToken(userEntity);
		final String refreshToken = jwtService.generateRefreshToken(userEntity);
		this.revokeAllUserTokens(userEntity);
		this.saveUserToken(userEntity, jwtToken);
		final BaseUserResponse userResponse = this.userMapper.toBaseUserResponse(userEntity);
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken)
				.userResponse(userResponse).build();
	}

	private void saveUserToken(UserEntity user, String jwtToken) {
		final TokenEntity token = TokenEntity.builder()
				.publicId(randomIDGenerator.generateRandomUUID())
				.user(user)
				.token(jwtToken)
				.tokenType(TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.build();
		tokenService.createToken(token);
	}

	private void revokeAllUserTokens(UserEntity user) {
		final List<TokenEntity> validUserTokens = tokenService.findAllValidTokensByUser(user.getId());
		if (validUserTokens.isEmpty())
			return;
		for (TokenEntity token : validUserTokens) {
			token.setExpired(true);
			token.setRevoked(true);
		}
		tokenService.createTokens(validUserTokens);
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String prefixAuthenticationHeader = String.format("%s ", HttpHeaders.AUTHORIZATION);
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith(prefixAuthenticationHeader)) {
			return;
		}
		refreshToken = authHeader.substring(prefixAuthenticationHeader.length());
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			final UserEntity user = userService.getUserByEmail(userEmail);
			if (jwtService.isTokenValid(refreshToken, user)) {
				final String accessToken = jwtService.generateToken(user);
				this.revokeAllUserTokens(user);
				this.saveUserToken(user, accessToken);
				final AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
						.accessToken(accessToken)
						.refreshToken(refreshToken)
						.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
			}
		}
	}

}
