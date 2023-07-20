package com.khanhnd.springshop.controller;

import com.khanhnd.springshop.domain.Manufacturer;
import com.khanhnd.springshop.dto.ManufacturerDto;
import com.khanhnd.springshop.service.FileStorageService;
import com.khanhnd.springshop.service.ManufacturerService;
import com.khanhnd.springshop.service.MapValidationErrorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manufacturers")
public class ManufacturerController {
    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
    MediaType.MULTIPART_FORM_DATA_VALUE},
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createManufacturer(@Valid @ModelAttribute ManufacturerDto dto, BindingResult result) {
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(result);
        System.out.println("test");
        if (responseEntity != null) {
            return responseEntity;
        }

        Manufacturer entity = manufacturerService.insertManufacturer(dto);

        dto.setId(entity.getId());
        dto.setLogo(entity.getLogo());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
