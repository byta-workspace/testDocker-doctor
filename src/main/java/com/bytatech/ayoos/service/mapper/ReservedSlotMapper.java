package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.ReservedSlotDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReservedSlot and its DTO ReservedSlotDTO.
 */
@Mapper(componentModel = "spring", uses = {DoctorMapper.class})
public interface ReservedSlotMapper extends EntityMapper<ReservedSlotDTO, ReservedSlot> {

    @Mapping(source = "doctor.id", target = "doctorId")
    ReservedSlotDTO toDto(ReservedSlot reservedSlot);

    @Mapping(source = "doctorId", target = "doctor")
    @Mapping(target = "statuses", ignore = true)
    ReservedSlot toEntity(ReservedSlotDTO reservedSlotDTO);

    default ReservedSlot fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReservedSlot reservedSlot = new ReservedSlot();
        reservedSlot.setId(id);
        return reservedSlot;
    }
}
