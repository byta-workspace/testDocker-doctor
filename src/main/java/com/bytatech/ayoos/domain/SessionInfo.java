package com.bytatech.ayoos.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A SessionInfo.
 */
@Entity
@Table(name = "session_info")
@Document(indexName = "sessioninfo")
public class SessionInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_name")
    private String sessionName;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "week_day")
    private Integer weekDay;

    @DecimalMin(value = "0")
    @Column(name = "from_time")
    private Double fromTime;

    @DecimalMax(value = "23")
    @Column(name = "to_time")
    private Double toTime;

    @Column(name = "jhi_interval")
    private Double interval;

    @ManyToOne
    @JsonIgnoreProperties("sessionInfos")
    private WorkPlace workPlace;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionName() {
        return sessionName;
    }

    public SessionInfo sessionName(String sessionName) {
        this.sessionName = sessionName;
        return this;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public LocalDate getDate() {
        return date;
    }

    public SessionInfo date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public SessionInfo weekDay(Integer weekDay) {
        this.weekDay = weekDay;
        return this;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
    }

    public Double getFromTime() {
        return fromTime;
    }

    public SessionInfo fromTime(Double fromTime) {
        this.fromTime = fromTime;
        return this;
    }

    public void setFromTime(Double fromTime) {
        this.fromTime = fromTime;
    }

    public Double getToTime() {
        return toTime;
    }

    public SessionInfo toTime(Double toTime) {
        this.toTime = toTime;
        return this;
    }

    public void setToTime(Double toTime) {
        this.toTime = toTime;
    }

    public Double getInterval() {
        return interval;
    }

    public SessionInfo interval(Double interval) {
        this.interval = interval;
        return this;
    }

    public void setInterval(Double interval) {
        this.interval = interval;
    }

    public WorkPlace getWorkPlace() {
        return workPlace;
    }

    public SessionInfo workPlace(WorkPlace workPlace) {
        this.workPlace = workPlace;
        return this;
    }

    public void setWorkPlace(WorkPlace workPlace) {
        this.workPlace = workPlace;
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
        SessionInfo sessionInfo = (SessionInfo) o;
        if (sessionInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sessionInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SessionInfo{" +
            "id=" + getId() +
            ", sessionName='" + getSessionName() + "'" +
            ", date='" + getDate() + "'" +
            ", weekDay=" + getWeekDay() +
            ", fromTime=" + getFromTime() +
            ", toTime=" + getToTime() +
            ", interval=" + getInterval() +
            "}";
    }
}
