package com.bytatech.ayoos.service.mapper;

import com.bytatech.ayoos.domain.*;
import com.bytatech.ayoos.service.dto.PaymentSettingsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PaymentSettings and its DTO PaymentSettingsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaymentSettingsMapper extends EntityMapper<PaymentSettingsDTO, PaymentSettings> {



    default PaymentSettings fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentSettings paymentSettings = new PaymentSettings();
        paymentSettings.setId(id);
        return paymentSettings;
    }
}
