package fr.instalite.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

	@Query(value = "SELECT t.* FROM token t INNER JOIN _user u ON t.user_id = u.id WHERE u.id = :userId AND (t.expired = false OR t.revoked = false)", nativeQuery = true)
	List<TokenEntity> findAllValidTokenByUser(@Param("userId") Long userId);

	Optional<TokenEntity> findByPublicId(String publicId);

	Optional<TokenEntity> findByToken(String token);

	List<TokenEntity> findAll();

	Page<TokenEntity> findAll(Pageable pageable);

	Long deleteByPublicId(String publicId);

}