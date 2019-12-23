package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.WorkPlaceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WorkPlace and its DTO WorkPlaceDTO.
 */
@Mapper(componentModel = "spring", uses = {DoctorMapper.class})
public interface WorkPlaceMapper extends EntityMapper<WorkPlaceDTO, WorkPlace> {

    @Mapping(source = "doctor.id", target = "doctorId")
    WorkPlaceDTO toDto(WorkPlace workPlace);

    @Mapping(source = "doctorId", target = "doctor")
    @Mapping(target = "sessionInfos", ignore = true)
    WorkPlace toEntity(WorkPlaceDTO workPlaceDTO);

    default WorkPlace fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkPlace workPlace = new WorkPlace();
        workPlace.setId(id);
        return workPlace;
    }
}
