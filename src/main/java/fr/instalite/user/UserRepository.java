package fr.instalite.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByPublicId(String publicId);

	Optional<UserEntity> findByEmail(String email);

	List<UserEntity> findAll();

	Page<UserEntity> findAll(Pageable pageable);

	Long deleteByPublicId(String publicId);

}
