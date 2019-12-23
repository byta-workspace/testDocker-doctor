package com.bytatech.ayoos.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Doctor.
 */
@Entity
@Table(name = "doctor")
@Document(indexName = "doctor")
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "doctor_id")
    private String doctorId;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "register_number")
    private String registerNumber;

    @Column(name = "practice_since")
    private LocalDate practiceSince;

    @Column(name = "total_rating")
    
    private Double totalRating;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private Long phoneNumber;

    @OneToOne
    @JoinColumn(unique = true)
    private ContactInfo contactInfo;

    @OneToOne
    @JoinColumn(unique = true)
    private PaymentSettings paymentSettings;

    @OneToOne
    @JoinColumn(unique = true)
    private DoctorSettings doctorSettings;

    @OneToMany(mappedBy = "doctor")
    private Set<WorkPlace> workPlaces = new HashSet<>();
    @OneToMany(mappedBy = "doctor")
    private Set<Qualification> qualifications = new HashSet<>();
    @OneToMany(mappedBy = "doctor")
    private Set<Review> reviews = new HashSet<>();
    @OneToMany(mappedBy = "doctor")
    private Set<UserRating> userRatings = new HashSet<>();
    @OneToMany(mappedBy = "doctor")
    private Set<ReservedSlot> reservedSlots = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public Doctor image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Doctor imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public Doctor doctorId(String doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public Doctor specialization(String specialization) {
        this.specialization = specialization;
        return this;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public Doctor registerNumber(String registerNumber) {
        this.registerNumber = registerNumber;
        return this;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public LocalDate getPracticeSince() {
        return practiceSince;
    }

    public Doctor practiceSince(LocalDate practiceSince) {
        this.practiceSince = practiceSince;
        return this;
    }

    public void setPracticeSince(LocalDate practiceSince) {
        this.practiceSince = practiceSince;
    }

    public Double getTotalRating() {
        return totalRating;
    }

    public Doctor totalRating(Double totalRating) {
        this.totalRating = totalRating;
        return this;
    }

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public String getFirstName() {
        return firstName;
    }

    public Doctor firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public Doctor email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public Doctor phoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public Doctor contactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
        return this;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public PaymentSettings getPaymentSettings() {
        return paymentSettings;
    }

    public Doctor paymentSettings(PaymentSettings paymentSettings) {
        this.paymentSettings = paymentSettings;
        return this;
    }

    public void setPaymentSettings(PaymentSettings paymentSettings) {
        this.paymentSettings = paymentSettings;
    }

    public DoctorSettings getDoctorSettings() {
        return doctorSettings;
    }

    public Doctor doctorSettings(DoctorSettings doctorSettings) {
        this.doctorSettings = doctorSettings;
        return this;
    }

    public void setDoctorSettings(DoctorSettings doctorSettings) {
        this.doctorSettings = doctorSettings;
    }

    public Set<WorkPlace> getWorkPlaces() {
        return workPlaces;
    }

    public Doctor workPlaces(Set<WorkPlace> workPlaces) {
        this.workPlaces = workPlaces;
        return this;
    }

    public Doctor addWorkPlace(WorkPlace workPlace) {
        this.workPlaces.add(workPlace);
        workPlace.setDoctor(this);
        return this;
    }

    public Doctor removeWorkPlace(WorkPlace workPlace) {
        this.workPlaces.remove(workPlace);
        workPlace.setDoctor(null);
        return this;
    }

    public void setWorkPlaces(Set<WorkPlace> workPlaces) {
        this.workPlaces = workPlaces;
    }

    public Set<Qualification> getQualifications() {
        return qualifications;
    }

    public Doctor qualifications(Set<Qualification> qualifications) {
        this.qualifications = qualifications;
        return this;
    }

    public Doctor addQualification(Qualification qualification) {
        this.qualifications.add(qualification);
        qualification.setDoctor(this);
        return this;
    }

    public Doctor removeQualification(Qualification qualification) {
        this.qualifications.remove(qualification);
        qualification.setDoctor(null);
        return this;
    }

    public void setQualifications(Set<Qualification> qualifications) {
        this.qualifications = qualifications;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public Doctor reviews(Set<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    public Doctor addReview(Review review) {
        this.reviews.add(review);
        review.setDoctor(this);
        return this;
    }

    public Doctor removeReview(Review review) {
        this.reviews.remove(review);
        review.setDoctor(null);
        return this;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<UserRating> getUserRatings() {
        return userRatings;
    }

    public Doctor userRatings(Set<UserRating> userRatings) {
        this.userRatings = userRatings;
        return this;
    }

    public Doctor addUserRating(UserRating userRating) {
        this.userRatings.add(userRating);
        userRating.setDoctor(this);
        return this;
    }

    public Doctor removeUserRating(UserRating userRating) {
        this.userRatings.remove(userRating);
        userRating.setDoctor(null);
        return this;
    }

    public void setUserRatings(Set<UserRating> userRatings) {
        this.userRatings = userRatings;
    }

    public Set<ReservedSlot> getReservedSlots() {
        return reservedSlots;
    }

    public Doctor reservedSlots(Set<ReservedSlot> reservedSlots) {
        this.reservedSlots = reservedSlots;
        return this;
    }

    public Doctor addReservedSlot(ReservedSlot reservedSlot) {
        this.reservedSlots.add(reservedSlot);
        reservedSlot.setDoctor(this);
        return this;
    }

    public Doctor removeReservedSlot(ReservedSlot reservedSlot) {
        this.reservedSlots.remove(reservedSlot);
        reservedSlot.setDoctor(null);
        return this;
    }

    public void setReservedSlots(Set<ReservedSlot> reservedSlots) {
        this.reservedSlots = reservedSlots;
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
        Doctor doctor = (Doctor) o;
        if (doctor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), doctor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Doctor{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", doctorId='" + getDoctorId() + "'" +
            ", specialization='" + getSpecialization() + "'" +
            ", registerNumber='" + getRegisterNumber() + "'" +
            ", practiceSince='" + getPracticeSince() + "'" +
            ", totalRating=" + getTotalRating() +
            ", firstName='" + getFirstName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            "}";
    }
}
