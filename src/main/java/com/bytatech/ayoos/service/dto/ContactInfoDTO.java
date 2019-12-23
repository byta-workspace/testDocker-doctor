package com.bytatech.ayoos.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ContactInfo entity.
 */
public class ContactInfoDTO implements Serializable {

    private Long id;

    private String facebookURL;

    private String twitterURL;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacebookURL() {
        return facebookURL;
    }

    public void setFacebookURL(String facebookURL) {
        this.facebookURL = facebookURL;
    }

    public String getTwitterURL() {
        return twitterURL;
    }

    public void setTwitterURL(String twitterURL) {
        this.twitterURL = twitterURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactInfoDTO contactInfoDTO = (ContactInfoDTO) o;
        if (contactInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contactInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContactInfoDTO{" +
            "id=" + getId() +
            ", facebookURL='" + getFacebookURL() + "'" +
            ", twitterURL='" + getTwitterURL() + "'" +
            "}";
    }
}
