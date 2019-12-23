package com.bytatech.ayoos.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Status entity.
 */
public class StatusDTO implements Serializable {

    private Long id;

    private String status;


    private Long reservedSlotId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getReservedSlotId() {
        return reservedSlotId;
    }

    public void setReservedSlotId(Long reservedSlotId) {
        this.reservedSlotId = reservedSlotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StatusDTO statusDTO = (StatusDTO) o;
        if (statusDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), statusDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StatusDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", reservedSlot=" + getReservedSlotId() +
            "}";
    }
}
