package com.khanhnd.springshop.repository;

import com.khanhnd.springshop.domain.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    List<Manufacturer> findByNameContainsIgnoreCase(String name);

    Page<Manufacturer> findByNameContainsIgnoreCase(String name, Pageable pageable);

    List<Manufacturer> findByIdNotAndNameContainsIgnoreCase(Long id, String name);

}