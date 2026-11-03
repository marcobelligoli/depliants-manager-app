package org.belligolifoundation.depliantsmanager.service.mapper;

import org.belligolifoundation.depliantsmanager.model.User;
import org.belligolifoundation.depliantsmanager.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);

    // id is never taken from the client: a new account must always be inserted, never merged into an existing one
    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO userDTO);
}
