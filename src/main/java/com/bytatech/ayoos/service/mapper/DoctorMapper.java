package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.DoctorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Doctor and its DTO DoctorDTO.
 */
@Mapper(componentModel = "spring", uses = {ContactInfoMapper.class, PaymentSettingsMapper.class, DoctorSettingsMapper.class})
public interface DoctorMapper extends EntityMapper<DoctorDTO, Doctor> {

    @Mapping(source = "contactInfo.id", target = "contactInfoId")
    @Mapping(source = "paymentSettings.id", target = "paymentSettingsId")
    @Mapping(source = "doctorSettings.id", target = "doctorSettingsId")
    DoctorDTO toDto(Doctor doctor);

    @Mapping(source = "contactInfoId", target = "contactInfo")
    @Mapping(source = "paymentSettingsId", target = "paymentSettings")
    @Mapping(source = "doctorSettingsId", target = "doctorSettings")
    @Mapping(target = "workPlaces", ignore = true)
    @Mapping(target = "qualifications", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "userRatings", ignore = true)
    @Mapping(target = "reservedSlots", ignore = true)
    Doctor toEntity(DoctorDTO doctorDTO);

    default Doctor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(id);
        return doctor;
    }
}
