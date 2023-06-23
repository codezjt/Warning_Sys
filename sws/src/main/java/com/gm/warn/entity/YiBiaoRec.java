package com.gm.warn.entity;


import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tzrqdata")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class YiBiaoRec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private String location;

    private Timestamp time;

    private Double wukuang;

    private Double wendu;

    private Double gongkuang;

    private Double yali;

    private Double total;

}
