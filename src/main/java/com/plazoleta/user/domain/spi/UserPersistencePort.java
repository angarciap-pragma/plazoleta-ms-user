package com.plazoleta.user.domain.spi;

import com.plazoleta.user.domain.model.User;

/**
 * Define la persistencia del agregado de usuario.
 */
public interface UserPersistencePort {

    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);

    User save(User user);
}
