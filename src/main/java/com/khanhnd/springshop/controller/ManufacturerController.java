package com.khanhnd.springshop.controller;

import com.khanhnd.springshop.domain.Manufacturer;
import com.khanhnd.springshop.dto.ManufacturerDto;
import com.khanhnd.springshop.exception.FileStorageException;
import com.khanhnd.springshop.service.FileStorageService;
import com.khanhnd.springshop.service.ManufacturerService;
import com.khanhnd.springshop.service.MapValidationErrorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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

        if (responseEntity != null) {
            return responseEntity;
        }

        Manufacturer entity = manufacturerService.insertManufacturer(dto);

        dto.setId(entity.getId());
        dto.setLogo(entity.getLogo());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PatchMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                        MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                        MediaType.MULTIPART_FORM_DATA_VALUE},
            produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateManufacturer(
            @PathVariable Long id,
            @Valid @ModelAttribute ManufacturerDto dto,
            BindingResult result) {
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(result);

        if (responseEntity != null) {
            return responseEntity;
        }

        Manufacturer entity = manufacturerService.updateManufacturer(id, dto);

        dto.setId(entity.getId());
        dto.setLogo(entity.getLogo());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/logo/{filename:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request) {
        Resource resource = fileStorageService.loadLogoFileAsResource(filename);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (Exception ex) {
            throw new FileStorageException("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""
                + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping
    public ResponseEntity<?> getManufacturers() {
        var list = manufacturerService.findAll();
        var newList = list.stream().map(item-> {
            ManufacturerDto dto = new ManufacturerDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(newList, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<?> getManufacturers(@RequestParam("query") String query,
                                              @PageableDefault(size = 2, sort = "name", direction = Sort.Direction.ASC)
                                              Pageable pageable) {
        var list = manufacturerService.findByName(query, pageable);
        var newList = list.getContent().stream().map(item-> {
            ManufacturerDto dto = new ManufacturerDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());

        var newPage = new PageImpl<ManufacturerDto>(newList, list.getPageable(), list.getTotalPages());

        return new ResponseEntity<>(newPage, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<?> getManufacturers(
            @PageableDefault(size = 5, sort = "name",direction = Sort.Direction.ASC)Pageable pageable) {
        var list = manufacturerService.findAll(pageable);
        var newList = list.stream().map(item-> {
            ManufacturerDto dto = new ManufacturerDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(newList, HttpStatus.OK);
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<?> getManufacturer(@PathVariable Long id) {
        var entity = manufacturerService.findById(id);
        ManufacturerDto dto = new ManufacturerDto();
        BeanUtils.copyProperties(entity, dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteManufacturer(@PathVariable Long id) {
        manufacturerService.deleteById(id);

        return new ResponseEntity<>("Category with id " + id + " was deleted", HttpStatus.OK);
    }
}
