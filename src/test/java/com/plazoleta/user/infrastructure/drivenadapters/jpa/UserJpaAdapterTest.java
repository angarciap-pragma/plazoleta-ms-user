package com.plazoleta.user.infrastructure.drivenadapters.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.model.UserRole;
import com.plazoleta.user.infrastructure.drivenadapters.jpa.entity.UserEntity;
import com.plazoleta.user.infrastructure.drivenadapters.jpa.mapper.UserEntityMapper;
import com.plazoleta.user.infrastructure.drivenadapters.jpa.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserJpaAdapterTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserEntityMapper userEntityMapper = Mockito.mock(UserEntityMapper.class);
    private final UserJpaAdapter adapter = new UserJpaAdapter(userRepository, userEntityMapper);

    @Test
    @DisplayName("should delegate email existence check")
    void shouldDelegateEmailExistenceCheck() {
        when(userRepository.existsByEmail("owner@plazoleta.com")).thenReturn(true);

        assertThat(adapter.existsByEmail("owner@plazoleta.com")).isTrue();
    }

    @Test
    @DisplayName("should delegate document existence check")
    void shouldDelegateDocumentExistenceCheck() {
        when(userRepository.existsByDocumentId("123456789")).thenReturn(true);

        assertThat(adapter.existsByDocumentId("123456789")).isTrue();
    }

    @Test
    @DisplayName("should save mapped user")
    void shouldSaveMappedUser() {
        User user = User.builder()
                .firstName("Andrea")
                .lastName("Garcia")
                .documentId("123456789")
                .phoneNumber("+573005698325")
                .birthDate(LocalDate.parse("1990-01-01"))
                .email("owner@plazoleta.com")
                .password("encoded")
                .role(UserRole.OWNER.name())
                .active(true)
                .build();
        UserEntity entity = UserEntity.builder().email("owner@plazoleta.com").active(true).build();
        User savedDomain = User.builder().id(1L).email("owner@plazoleta.com").role(UserRole.OWNER.name()).active(true).build();

        when(userEntityMapper.toEntity(user)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userEntityMapper.toDomain(entity)).thenReturn(savedDomain);

        assertThat(adapter.save(user).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should find mapped user by id")
    void shouldFindMappedUserById() {
        UserEntity entity = UserEntity.builder().id(7L).email("owner@plazoleta.com").active(true).build();
        User user = User.builder().id(7L).email("owner@plazoleta.com").role(UserRole.OWNER.name()).active(true).build();

        when(userRepository.findById(7L)).thenReturn(Optional.of(entity));
        when(userEntityMapper.toDomain(entity)).thenReturn(user);

        assertThat(adapter.findById(7L)).isPresent();
    }

    @Test
    @DisplayName("should find mapped user by email")
    void shouldFindMappedUserByEmail() {
        UserEntity entity = UserEntity.builder().id(7L).email("owner@plazoleta.com").active(true).build();
        User user = User.builder().id(7L).email("owner@plazoleta.com").role(UserRole.OWNER.name()).active(true).build();

        when(userRepository.findByEmail("owner@plazoleta.com")).thenReturn(Optional.of(entity));
        when(userEntityMapper.toDomain(entity)).thenReturn(user);

        assertThat(adapter.findByEmail("owner@plazoleta.com")).isPresent();
    }
}
