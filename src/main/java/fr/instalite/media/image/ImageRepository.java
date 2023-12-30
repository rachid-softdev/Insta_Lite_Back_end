package fr.instalite.media.image;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.instalite.common.EVisibility;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

	Optional<ImageEntity> findByPublicId(String publicId);

	Optional<ImageEntity> findByTitle(String title);

	Page<ImageEntity> findByVisibilityIn(Pageable pageable, List<EVisibility> visibilities);

	Page<ImageEntity> findByAuthorEmailOrVisibilityIn(Pageable pageable, String email, List<EVisibility> visibilities);

	List<ImageEntity> findAll();

	Page<ImageEntity> findAll(Pageable pageable);

	Long deleteByPublicId(String publicId);

}
