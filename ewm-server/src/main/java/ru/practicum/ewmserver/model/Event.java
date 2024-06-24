package ru.practicum.ewmserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import ru.practicum.ewmserver.enums.EventState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "EVENTS", schema = "PUBLIC")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @Column(name = "EVENT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(nullable = false)
    protected String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "CATEGORY_ID")
    protected Category category;
    @Column(name = "CREATED_ON", nullable = false)
    protected LocalDateTime createdOn;
    @Column(nullable = false)
    protected String description;
    @Column(name = "EVENT_DATE", nullable = false)
    protected LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "INITIATOR_ID")
    protected User initiator;
    @OneToOne
    @JoinColumn(name = "LOCATION_ID")
    protected Location location;
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    protected Boolean paid;
    @Column(name = "PARTICIPANT_LIMIT", nullable = false)
    protected int participantLimit;
    @Column(name = "PUBLISHED_ON", nullable = false)
    protected LocalDateTime publishedOn;
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    protected Boolean requestModeration;
    @Enumerated(EnumType.ORDINAL)
    protected EventState state;
    @Column(nullable = false)
    protected String title;
}
