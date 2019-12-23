package com.bytatech.ayoos.domain;



import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DoctorSettings.
 */
@Entity
@Table(name = "doctor_settings")
@Document(indexName = "doctorsettings")
public class DoctorSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "approval_type")
    private String approvalType;

    @Column(name = "is_mail_notifications_enabled")
    private Boolean isMailNotificationsEnabled;

    @Column(name = "is_sms_notifications_enabled")
    private Boolean isSMSNotificationsEnabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public DoctorSettings approvalType(String approvalType) {
        this.approvalType = approvalType;
        return this;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    public Boolean isIsMailNotificationsEnabled() {
        return isMailNotificationsEnabled;
    }

    public DoctorSettings isMailNotificationsEnabled(Boolean isMailNotificationsEnabled) {
        this.isMailNotificationsEnabled = isMailNotificationsEnabled;
        return this;
    }

    public void setIsMailNotificationsEnabled(Boolean isMailNotificationsEnabled) {
        this.isMailNotificationsEnabled = isMailNotificationsEnabled;
    }

    public Boolean isIsSMSNotificationsEnabled() {
        return isSMSNotificationsEnabled;
    }

    public DoctorSettings isSMSNotificationsEnabled(Boolean isSMSNotificationsEnabled) {
        this.isSMSNotificationsEnabled = isSMSNotificationsEnabled;
        return this;
    }

    public void setIsSMSNotificationsEnabled(Boolean isSMSNotificationsEnabled) {
        this.isSMSNotificationsEnabled = isSMSNotificationsEnabled;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DoctorSettings doctorSettings = (DoctorSettings) o;
        if (doctorSettings.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), doctorSettings.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DoctorSettings{" +
            "id=" + getId() +
            ", approvalType='" + getApprovalType() + "'" +
            ", isMailNotificationsEnabled='" + isIsMailNotificationsEnabled() + "'" +
            ", isSMSNotificationsEnabled='" + isIsSMSNotificationsEnabled() + "'" +
            "}";
    }
}
