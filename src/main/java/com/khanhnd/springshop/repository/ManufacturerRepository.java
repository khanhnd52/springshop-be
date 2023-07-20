package com.khanhnd.springshop.repository;

import com.khanhnd.springshop.domain.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    List<Manufacturer> findByNameContainsIgnoreCase(String name);

    List<Manufacturer> findByIdNotAndNameContainsIgnoreCase(Long id, String name);

}