package fr.instalite.token;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class TokenMapperImpl implements TokenMapper, Serializable {

	private static final long serialVersionUID = 1L;

	public TokenResponse toTokenResponse(TokenEntity tokenEntity) {
		if (tokenEntity == null) {
			return new TokenResponse();
		}
		final TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setToken(tokenEntity.getToken());
		tokenResponse.setTokenType(tokenEntity.getTokenType());
		tokenResponse.setRevoked(tokenEntity.isRevoked());
		tokenResponse.setExpired(tokenEntity.isExpired());
		tokenResponse.setUser(tokenEntity.getUser());
		return tokenResponse;
	}

	public List<TokenResponse> toTokenResponseList(List<TokenEntity> tokenEntities) {
		if (tokenEntities == null) {
			return new ArrayList<TokenResponse>();
		}
		List<TokenResponse> tokenResponses = new ArrayList<>();
		for (TokenEntity tokenEntity : tokenEntities) {
			tokenResponses.add(toTokenResponse(tokenEntity));
		}
		return tokenResponses;
	}

}
