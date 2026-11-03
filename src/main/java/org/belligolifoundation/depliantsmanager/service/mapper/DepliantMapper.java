package org.belligolifoundation.depliantsmanager.service.mapper;

import org.belligolifoundation.depliantsmanager.model.Depliant;
import org.belligolifoundation.depliantsmanager.model.User;
import org.belligolifoundation.depliantsmanager.model.dto.DepliantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepliantMapper {

    DepliantMapper INSTANCE = Mappers.getMapper(DepliantMapper.class);

    @Mapping(source = "user.id", target = "userId")
    DepliantDTO toDTO(Depliant depliant);

    // id is never taken from the client: creating a depliant must not merge into (and re-own) an existing row
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", expression = "java(mapUserId(depliantDTO.getUserId()))")
    Depliant toEntity(DepliantDTO depliantDTO);

    default User mapUserId(Long id) {
        if (id == null) {
            return null;
        }

        User user = new User();
        user.setId(id);
        return user;
    }
}
