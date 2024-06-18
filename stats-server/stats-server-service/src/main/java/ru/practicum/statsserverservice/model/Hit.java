package ru.practicum.statsserverservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "HITS", schema = "PUBLIC")
@Getter
@Setter
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

    public Hit(int id, String app, String uri, String ip, LocalDateTime moment) {
        this.id = id;
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.moment = moment;
    }

    public Hit(String app, String uri, String ip, LocalDateTime moment) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.moment = moment;
    }

    public Hit() {
    }
}
