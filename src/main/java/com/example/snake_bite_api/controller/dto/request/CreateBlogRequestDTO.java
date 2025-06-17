package com.example.snake_bite_api.controller.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateBlogRequestDTO {

    private String title;
    private String content;
    private List<MultipartFile> imageFiles;
}
