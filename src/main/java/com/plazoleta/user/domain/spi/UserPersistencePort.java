package com.plazoleta.user.domain.spi;

import com.plazoleta.user.domain.model.User;
import java.util.Optional;

/**
 * Define la persistencia del agregado de usuario.
 */
public interface UserPersistencePort {

    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    User save(User user);
}
