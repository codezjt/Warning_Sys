package com.gm.warn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;


import javax.persistence.*;

@Data
@Entity
@Table(name = "cacl")
@ToString
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class CaclData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String name;

    private int cacl;

}
