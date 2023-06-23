package com.gm.warn.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "pointer_config")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class YiBiaoPointParm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String path;

    private String template_path;

    private String location;

    private Integer configured;

    private Double start;

    private Double end;

    private Integer start_x;

    private Integer start_y;

    private Integer center_x;

    private Integer center_y;

    private Integer end_x;

    private Integer end_y;

    private Double min;

    private Double max;


}
