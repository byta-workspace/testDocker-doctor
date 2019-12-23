package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.QualificationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Qualification and its DTO QualificationDTO.
 */
@Mapper(componentModel = "spring", uses = {DoctorMapper.class})
public interface QualificationMapper extends EntityMapper<QualificationDTO, Qualification> {

    @Mapping(source = "doctor.id", target = "doctorId")
    QualificationDTO toDto(Qualification qualification);

    @Mapping(source = "doctorId", target = "doctor")
    Qualification toEntity(QualificationDTO qualificationDTO);

    default Qualification fromId(Long id) {
        if (id == null) {
            return null;
        }
        Qualification qualification = new Qualification();
        qualification.setId(id);
        return qualification;
    }
}
