package com.bytatech.ayoos.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Qualification entity.
 */
public class QualificationDTO implements Serializable {

    private Long id;

    private String qualification;


    private Long doctorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QualificationDTO qualificationDTO = (QualificationDTO) o;
        if (qualificationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), qualificationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QualificationDTO{" +
            "id=" + getId() +
            ", qualification='" + getQualification() + "'" +
            ", doctor=" + getDoctorId() +
            "}";
    }
}
