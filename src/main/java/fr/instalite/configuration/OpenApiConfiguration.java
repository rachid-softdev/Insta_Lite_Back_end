package fr.instalite.configuration;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(contact = @Contact(name = "InstaLite", email = "instalite@instalite.com", url = "https://localhost:8080/api/insta_lite"), description = "OpenApi documentation pour l'application InstaLite", title = "OpenApi Specification - InstaLite", version = "1.0", license = @License(name = "Licence name", url = "https://some-url.com"), termsOfService = "Terms of service"), servers = {
		@Server(description = "Local Environment", url = "http://localhost:8080/api/insta_lite"),
		@Server(description = "Production Environment", url = "http://localhost:8080/api/insta_lite") }, security = {
				@SecurityRequirement(name = "bearerAuth") }

)
@SecurityScheme(name = "bearerAuth", description = "JWT auth description", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
@SecurityScheme(name = "bearerAuth", description = "JWT auth description", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class OpenApiConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

}
