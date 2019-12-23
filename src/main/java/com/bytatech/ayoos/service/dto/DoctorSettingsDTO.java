package com.bytatech.ayoos.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DoctorSettings entity.
 */
public class DoctorSettingsDTO implements Serializable {

    private Long id;

    private String approvalType;

    private Boolean isMailNotificationsEnabled;

    private Boolean isSMSNotificationsEnabled;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    public Boolean isIsMailNotificationsEnabled() {
        return isMailNotificationsEnabled;
    }

    public void setIsMailNotificationsEnabled(Boolean isMailNotificationsEnabled) {
        this.isMailNotificationsEnabled = isMailNotificationsEnabled;
    }

    public Boolean isIsSMSNotificationsEnabled() {
        return isSMSNotificationsEnabled;
    }

    public void setIsSMSNotificationsEnabled(Boolean isSMSNotificationsEnabled) {
        this.isSMSNotificationsEnabled = isSMSNotificationsEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DoctorSettingsDTO doctorSettingsDTO = (DoctorSettingsDTO) o;
        if (doctorSettingsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), doctorSettingsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DoctorSettingsDTO{" +
            "id=" + getId() +
            ", approvalType='" + getApprovalType() + "'" +
            ", isMailNotificationsEnabled='" + isIsMailNotificationsEnabled() + "'" +
            ", isSMSNotificationsEnabled='" + isIsSMSNotificationsEnabled() + "'" +
            "}";
    }
}
