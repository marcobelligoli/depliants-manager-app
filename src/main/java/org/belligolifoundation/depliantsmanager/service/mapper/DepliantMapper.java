package org.belligolifoundation.depliantsmanager.service.mapper;

import org.belligolifoundation.depliantsmanager.model.Depliant;
import org.belligolifoundation.depliantsmanager.model.dto.DepliantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepliantMapper {

    DepliantMapper INSTANCE = Mappers.getMapper(DepliantMapper.class);

    DepliantDTO toDTO(Depliant depliant);
    Depliant toEntity(DepliantDTO depliantDTO);
}
