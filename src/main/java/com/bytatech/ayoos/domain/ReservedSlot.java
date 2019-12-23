package com.bytatech.ayoos.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ReservedSlot.
 */
@Entity
@Table(name = "reserved_slot")
@Document(indexName = "reservedslot")
public class ReservedSlot implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "start_time")
    private Double startTime;

    @Column(name = "end_time")
    private Double endTime;

    @Column(name = "token_number")
    private Integer tokenNumber;

    @ManyToOne
    @JsonIgnoreProperties("reservedSlots")
    private Doctor doctor;

    @OneToMany(mappedBy = "reservedSlot")
    private Set<Status> statuses = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservedSlot date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getStartTime() {
        return startTime;
    }

    public ReservedSlot startTime(Double startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public ReservedSlot endTime(Double endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public Integer getTokenNumber() {
        return tokenNumber;
    }

    public ReservedSlot tokenNumber(Integer tokenNumber) {
        this.tokenNumber = tokenNumber;
        return this;
    }

    public void setTokenNumber(Integer tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public ReservedSlot doctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Set<Status> getStatuses() {
        return statuses;
    }

    public ReservedSlot statuses(Set<Status> statuses) {
        this.statuses = statuses;
        return this;
    }

    public ReservedSlot addStatus(Status status) {
        this.statuses.add(status);
        status.setReservedSlot(this);
        return this;
    }

    public ReservedSlot removeStatus(Status status) {
        this.statuses.remove(status);
        status.setReservedSlot(null);
        return this;
    }

    public void setStatuses(Set<Status> statuses) {
        this.statuses = statuses;
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
        ReservedSlot reservedSlot = (ReservedSlot) o;
        if (reservedSlot.getId() == null || getId() == null) {
            return false;
        }
       
        return (getStartTime()==reservedSlot.getStartTime()&&getDate()==reservedSlot.getDate()&&getDate()==reservedSlot.getDate()&&getDoctor()==reservedSlot.getDoctor());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReservedSlot{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", startTime=" + getStartTime() +
            ", endTime=" + getEndTime() +
            ", tokenNumber=" + getTokenNumber() +
            "}";
    }
}
