package ru.practicum.statsserverservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "HITS", schema = "PUBLIC")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hit {
    @Id
    @Column(name = "HIT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(nullable = false)
    protected String app;
    @Column(nullable = false)
    protected String uri;
    @Column(nullable = false)
    protected String ip;
    @Column(nullable = false)
    protected LocalDateTime moment;
}
