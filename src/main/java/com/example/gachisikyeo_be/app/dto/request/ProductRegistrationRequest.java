package com.example.gachisikyeo_be.app.dto.request;

import com.example.gachisikyeo_be.app.domain.productRegistration.ProductCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRegistrationRequest {

    @NotNull
    private ProductCategory category;

    @NotBlank
    private String productName;

    @Min(0)
    private long price;

    @Min(0)
    private int stockQuantity;

    @Min(1)
    private int unitQuantity;

    @NotBlank
    private String descriptionTitle;

    @NotBlank
    private String description;
}
