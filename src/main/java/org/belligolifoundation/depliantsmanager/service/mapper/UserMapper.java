package org.belligolifoundation.depliantsmanager.service.mapper;

import org.belligolifoundation.depliantsmanager.model.User;
import org.belligolifoundation.depliantsmanager.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}
