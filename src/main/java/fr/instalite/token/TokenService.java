package fr.instalite.token;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.instalite.common.RandomIDGenerator;

@Service
public class TokenService implements Serializable {

	private static final long serialVersionUID = 1L;

	private final TokenRepository tokenRepository;
	private final RandomIDGenerator randomIDGenerator;

	public TokenService(@Autowired TokenRepository tokenRepository, @Autowired RandomIDGenerator randomIDGenerator) {
		this.tokenRepository = tokenRepository;
		this.randomIDGenerator = randomIDGenerator;
	}

	public TokenEntity getTokenById(Long id) {
		return tokenRepository.findById(id)
				.orElseThrow(() -> new TokenNotFoundException("Token not found",
						String.format("Le token avec l'id %d n'a pas été trouvé.", id), HttpStatus.NOT_FOUND));
	}

	public TokenEntity getTokenByPublicId(String publicId) {
		return tokenRepository.findByPublicId(publicId)
				.orElseThrow(() -> new TokenNotFoundException("Token not found",
						String.format("Le token avec l'id %d n'a pas été trouvé.", publicId), HttpStatus.NOT_FOUND));
	}

	public TokenEntity getTokenByValue(String token) {
		return tokenRepository.findByToken(token)
				.orElseThrow(() -> new TokenNotFoundException("Token not found",
						String.format("Le token n'a pas été trouvé. (%s)", token), HttpStatus.NOT_FOUND));
	}

	public List<TokenEntity> getAllTokens() {
		return tokenRepository.findAll();
	}

	public List<TokenEntity> findAllValidTokensByUser(Long userId) {
		return tokenRepository.findAllValidTokenByUser(userId);
	}

	@Transactional
	public TokenEntity createToken(TokenEntity token) {
		if (token != null && token.getPublicId() != null) {
			token.setPublicId(randomIDGenerator.generateRandomUUID());
			token.setCreatedAt(Instant.now());
			token.setUpdatedAt(Instant.now());
		}
		return tokenRepository.save(token);
	}

	@Transactional
	public List<TokenEntity> createTokens(Iterable<TokenEntity> tokensEntities) {
		for (TokenEntity tokenEntity : tokensEntities) {
			tokenEntity.setPublicId(randomIDGenerator.generateRandomUUID());
			tokenEntity.setCreatedAt(Instant.now());
			tokenEntity.setUpdatedAt(Instant.now());
		}
		return tokenRepository.saveAll(tokensEntities);
	}

	@Transactional
	public TokenEntity updateTokenById(Long id, TokenEntity token) {
		final TokenEntity tokenToUpdate = tokenRepository.findById(id)
				.orElseThrow(() -> new TokenNotFoundException("Token not found",
						String.format("Le token avec l'id %d n'a pas été trouvé.", id), HttpStatus.NOT_FOUND));
		return tokenRepository.save(TokenEntity.toTokenEntity(tokenToUpdate));
	}

	@Transactional
	public TokenEntity updateTokenByPublicId(String publicId, TokenEntity token) {
		final TokenEntity tokenToUpdate = tokenRepository.findByPublicId(publicId)
				.orElseThrow(() -> new TokenNotFoundException("Token not found",
						String.format("Le token avec l'identifiant publique %s n'a pas été trouvé.", publicId),
						HttpStatus.NOT_FOUND));
		return tokenRepository.save(TokenEntity.toTokenEntity(tokenToUpdate));
	}

	@Transactional
	public void deleteTokenById(Long id) {
		final TokenEntity existingToken = tokenRepository.findById(id)
				.orElseThrow(() -> new TokenNotFoundException("Token not found",
						String.format("Le token avec l'identifiant publique %d n'a pas été trouvé.", id),
						HttpStatus.NOT_FOUND));
		tokenRepository.deleteById(existingToken.getId());
	}

	@Transactional
	public void deleteByPublicId(String publicId) {
		final TokenEntity existingToken = tokenRepository.findByPublicId(publicId)
				.orElseThrow(() -> new TokenNotFoundException("Token not found",
						String.format("Le token avec l'identifiant publique %s n'a pas été trouvé.", publicId),
						HttpStatus.NOT_FOUND));
		tokenRepository.deleteByPublicId(existingToken.getPublicId());
	}

	@Transactional
	public void deleteAllTokens() {
		if (tokenRepository.count() == 0) {
			throw new TokenNotFoundException("Token not found",
					String.format("Aucun token trouvé."),
					HttpStatus.NOT_FOUND);
		}
		tokenRepository.deleteAll();
	}

}
