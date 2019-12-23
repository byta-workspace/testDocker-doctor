package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.SessionInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SessionInfo and its DTO SessionInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {WorkPlaceMapper.class})
public interface SessionInfoMapper extends EntityMapper<SessionInfoDTO, SessionInfo> {

    @Mapping(source = "workPlace.id", target = "workPlaceId")
    SessionInfoDTO toDto(SessionInfo sessionInfo);

    @Mapping(source = "workPlaceId", target = "workPlace")
    SessionInfo toEntity(SessionInfoDTO sessionInfoDTO);

    default SessionInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setId(id);
        return sessionInfo;
    }
}
