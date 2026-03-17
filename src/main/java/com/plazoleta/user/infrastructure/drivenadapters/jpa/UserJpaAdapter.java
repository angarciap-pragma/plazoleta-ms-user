package com.plazoleta.user.infrastructure.drivenadapters.jpa;

import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.spi.UserPersistencePort;
import com.plazoleta.user.infrastructure.drivenadapters.jpa.mapper.UserEntityMapper;
import com.plazoleta.user.infrastructure.drivenadapters.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementa la persistencia de usuarios usando JPA.
 */
@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserPersistencePort {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDocumentId(final String documentId) {
        return userRepository.existsByDocumentId(documentId);
    }

    @Override
    public User save(final User user) {
        return userEntityMapper.toDomain(userRepository.save(userEntityMapper.toEntity(user)));
    }
}
