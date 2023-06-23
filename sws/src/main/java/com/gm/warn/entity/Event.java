package com.gm.warn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;


@Data
@Entity
@Table(name = "event")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Title of the event.
     */
    private String name;

    /**
     * Author name.
     */
    private String parties;

    /**
     * Publication date.
     */
    private String date;

    /**
     * Press.
     */
    private String road;

    /**
     * Abstract of the event.
     */
    private String note;

    /**
     * The url of the event's cover.
     */
    private String cover;

    /**
     * warning or not
     */
    private String iswarning;

    /**
     * Category id.
     */
    @ManyToOne
    @JoinColumn(name="cid")
    private Category category;
}
