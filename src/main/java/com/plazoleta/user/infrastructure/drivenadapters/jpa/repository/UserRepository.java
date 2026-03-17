package com.plazoleta.user.infrastructure.drivenadapters.jpa.repository;

import com.plazoleta.user.infrastructure.drivenadapters.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Expone el acceso JPA a la tabla de usuarios.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);
}
