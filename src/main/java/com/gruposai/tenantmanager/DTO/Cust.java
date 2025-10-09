package com.gruposai.tenantmanager.DTO;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cust")
public class Cust {
    @Id
    private String id_n;
    // Puedes dejarlo vacío o agregar otros campos si quieres
}
