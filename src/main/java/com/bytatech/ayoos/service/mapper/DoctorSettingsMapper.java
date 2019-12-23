package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.DoctorSettingsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DoctorSettings and its DTO DoctorSettingsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DoctorSettingsMapper extends EntityMapper<DoctorSettingsDTO, DoctorSettings> {



    default DoctorSettings fromId(Long id) {
        if (id == null) {
            return null;
        }
        DoctorSettings doctorSettings = new DoctorSettings();
        doctorSettings.setId(id);
        return doctorSettings;
    }
}
