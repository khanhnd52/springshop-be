package com.khanhnd.springshop.service;

import com.khanhnd.springshop.domain.Manufacturer;
import com.khanhnd.springshop.dto.ManufacturerDto;
import com.khanhnd.springshop.exception.ManufacturerException;
import com.khanhnd.springshop.repository.ManufacturerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<?> findAll() {
        return manufacturerRepository.findAll();
    }

    public Page<Manufacturer> findAll(Pageable pageable) {
        return manufacturerRepository.findAll(pageable);
    }

    public Manufacturer findById(Long id) {
        Optional<Manufacturer> found = manufacturerRepository.findById(id);

        if (found.isEmpty()) {
            throw new ManufacturerException("Manufacturer with id " + id + " does not existed");
        }
        return found.get();
    }

    public void deleteById(Long id) {
        Manufacturer existed = findById(id);

        manufacturerRepository.delete(existed);
    }
}
