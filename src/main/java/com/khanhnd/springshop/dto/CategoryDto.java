package com.khanhnd.springshop.dto;

import com.khanhnd.springshop.domain.CategoryStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.khanhnd.springshop.domain.Category}
 */
@Data
public class CategoryDto implements Serializable {
    Long id;
    @NotEmpty(message = "Category name is required")
    String name;
    CategoryStatus status;
}