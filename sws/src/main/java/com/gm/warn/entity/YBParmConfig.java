package com.gm.warn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "led_config_items")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class YBParmConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private Integer config_id;

    private String name;

    private Integer x;

    private Integer y;

    private Integer width;

    private Integer height;

    private Double min;

    private Double max;
}
