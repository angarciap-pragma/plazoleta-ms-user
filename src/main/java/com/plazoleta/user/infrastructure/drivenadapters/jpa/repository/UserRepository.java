package com.plazoleta.user.infrastructure.drivenadapters.jpa.repository;

import com.plazoleta.user.infrastructure.drivenadapters.jpa.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Expone el acceso JPA a la tabla de usuarios.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);

    Optional<UserEntity> findByEmail(String email);
}
