package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.UserRatingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserRating and its DTO UserRatingDTO.
 */
@Mapper(componentModel = "spring", uses = {DoctorMapper.class})
public interface UserRatingMapper extends EntityMapper<UserRatingDTO, UserRating> {

    @Mapping(source = "doctor.id", target = "doctorId")
    UserRatingDTO toDto(UserRating userRating);

    @Mapping(source = "doctorId", target = "doctor")
    UserRating toEntity(UserRatingDTO userRatingDTO);

    default UserRating fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserRating userRating = new UserRating();
        userRating.setId(id);
        return userRating;
    }
}
