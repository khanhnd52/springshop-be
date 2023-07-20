package com.khanhnd.springshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO for {@link com.khanhnd.springshop.domain.Manufacturer}
 */
@Data
public class ManufacturerDto implements Serializable {
    private Long id;
    private String name;
    private String logo;

    @JsonIgnore
    private MultipartFile logoFile;


}