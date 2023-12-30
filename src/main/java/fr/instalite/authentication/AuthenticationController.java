package fr.instalite.authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/insta_lite/authentication")
public class AuthenticationController {

	private final AuthenticationService service;

	public AuthenticationController(@Autowired AuthenticationService service) {
		this.service = service;
	}

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
		return ResponseEntity.ok(service.register(request));
	}

	@PostMapping({"/authenticate"})
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticate(request));
	}

	@PostMapping("/refresh-token")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		service.refreshToken(request, response);
	}
	
	@GetMapping("/check-authentication")
    public ResponseEntity<Map<String, Object>> checkAuthentication(Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		if (authentication != null && authentication.isAuthenticated()) {
			Object userDetails = authentication.getDetails();
			response.put("is_authenticated", true);
			response.put("user", userDetails);
			response.put("message", "L'utilisateur est connecté");
			return ResponseEntity.ok(response);
		}
		response.put("is_authenticated", false);
		response.put("user", null);
		response.put("message", "L'utilisateur n'est pas connecté");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

}
