package com.bytatech.ayoos.service.dto;
import java.io.Serializable;
import java.util.Objects;

import org.springframework.data.elasticsearch.annotations.GeoPointField;

/**
 * A DTO for the WorkPlace entity.
 */
public class WorkPlaceDTO implements Serializable {

    private Long id;

    private String name;

    private String locationName;

    @GeoPointField
    private String location;


    private Long doctorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

        WorkPlaceDTO workPlaceDTO = (WorkPlaceDTO) o;
        if (workPlaceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workPlaceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkPlaceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", locationName='" + getLocationName() + "'" +
            ", location='" + getLocation() + "'" +
            ", doctor=" + getDoctorId() +
            "}";
    }
}
