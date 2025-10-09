package com.gruposai.tenantmanager.Repository;

import com.gruposai.tenantmanager.DTO.Cust;
import com.gruposai.tenantmanager.DTO.CustomerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Cust, String> {
    @Query(
            value = "SELECT " +
                    "c.id_n AS id_n, " +
                    "TRIM(c.company) AS company, " +
                    "TRIM(c.addr1) AS addr1, " +
                    "TRIM(c.phone1) AS phone1, " +
                    "TRIM(c.phone2) AS phone2, " +
                    "TRIM(c.cv) AS cv, " +
                    "TRIM(s.email) AS email " +
                    "FROM cust c " +
                    "INNER JOIN shipto s ON c.id_n = s.id_n " +
                    "WHERE c.id_n = :id_n",
            nativeQuery = true
    )

    List<CustomerProjection> findByIdN(@Param("id_n") String id_n);
}

