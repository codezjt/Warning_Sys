package com.gm.warn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "pointer_data")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class YBPointData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private Integer pointer_id;

    private String location;

    private Timestamp time;

    private String value;
}
