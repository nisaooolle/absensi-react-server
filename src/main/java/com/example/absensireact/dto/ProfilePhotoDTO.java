package com.example.absensireact.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

@ApiModel(description = "Data for uploading profile photo")
public class ProfilePhotoDTO {

    @ApiModelProperty(value = "Profile picture", required = true)
    private MultipartFile picture;

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
