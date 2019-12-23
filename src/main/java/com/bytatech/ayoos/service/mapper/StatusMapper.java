package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.StatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Status and its DTO StatusDTO.
 */
@Mapper(componentModel = "spring", uses = {ReservedSlotMapper.class})
public interface StatusMapper extends EntityMapper<StatusDTO, Status> {

    @Mapping(source = "reservedSlot.id", target = "reservedSlotId")
    StatusDTO toDto(Status status);

    @Mapping(source = "reservedSlotId", target = "reservedSlot")
    Status toEntity(StatusDTO statusDTO);

    default Status fromId(Long id) {
        if (id == null) {
            return null;
        }
        Status status = new Status();
        status.setId(id);
        return status;
    }
}
