package com.plazoleta.user.infrastructure.drivenadapters.jpa.mapper;

import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.infrastructure.drivenadapters.jpa.entity.UserEntity;
import org.mapstruct.Mapper;

/**
 * Mapea entre el agregado de dominio y la entidad JPA.
 */
@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    default UserEntity toEntity(final User user) {
        return UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .documentId(user.getDocumentId())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .active(user.isActive())
                .restaurantId(user.getRestaurantId())
                .build();
    }

    default User toDomain(final UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .documentId(entity.getDocumentId())
                .phoneNumber(entity.getPhoneNumber())
                .birthDate(entity.getBirthDate())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .active(entity.isActive())
                .restaurantId(entity.getRestaurantId())
                .build();
    }
}
