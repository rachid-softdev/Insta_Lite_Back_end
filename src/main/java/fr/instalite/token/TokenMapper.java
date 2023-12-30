package fr.instalite.token;

import java.util.List;

public interface TokenMapper {

	public TokenResponse toTokenResponse(TokenEntity tokenEntity);

	public List<TokenResponse> toTokenResponseList(List<TokenEntity> tokenEntities);

}
