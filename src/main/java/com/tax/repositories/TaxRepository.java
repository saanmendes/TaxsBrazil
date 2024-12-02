package com.tax.repositories;


import com.tax.models.TaxType;
import org.springframework.data.jpa.repository.JpaRepository;



public interface TaxRepository extends JpaRepository<TaxType,Long> {

}


