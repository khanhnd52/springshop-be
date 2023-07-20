package com.khanhnd.springshop.service;

import com.khanhnd.springshop.domain.Manufacturer;
import com.khanhnd.springshop.dto.ManufacturerDto;
import com.khanhnd.springshop.exception.ManufacturerException;
import com.khanhnd.springshop.repository.ManufacturerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public Manufacturer insertManufacturer(ManufacturerDto dto) {
        List<?> foundedList = manufacturerRepository.findByNameContainsIgnoreCase(dto.getName());

        if (foundedList.size() > 0) {
            throw new ManufacturerException("Manufacturer name is existed");
        }

        Manufacturer entity = new Manufacturer();
        BeanUtils.copyProperties(dto, entity);

        if (dto.getLogoFile() != null) {
            String filename = fileStorageService.storeLogoFile(dto.getLogoFile());

            entity.setLogo(filename);
            dto.setLogoFile(null);
        }
        return manufacturerRepository.save(entity);
    }
}
