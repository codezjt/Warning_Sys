package com.gm.warn.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "led_config")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class YiBiaoPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String path;

    private String template_path;

    private String data_table;

    private String location;


}
