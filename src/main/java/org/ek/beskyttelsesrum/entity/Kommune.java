package org.ek.beskyttelsesrum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Kommune {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String navn;
    private String kode; //Kommune kode fra API

    public Kommune(String kode, String navn) {
        this.navn = navn;
        this.kode = kode;
    }




}
