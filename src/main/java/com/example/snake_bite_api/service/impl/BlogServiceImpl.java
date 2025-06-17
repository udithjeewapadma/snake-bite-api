package com.example.snake_bite_api.service.impl;

import com.cloudinary.Cloudinary;
import com.example.snake_bite_api.controller.dto.request.CreateBlogRequestDTO;
import com.example.snake_bite_api.controller.dto.response.BlogResponseDTO;
import com.example.snake_bite_api.exception.BlogNotFoundException;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.Blog;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.repository.BlogRepository;
import com.example.snake_bite_api.repository.UserRepository;
import com.example.snake_bite_api.service.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;


    @Override
    public BlogResponseDTO createBlog(Long userId, CreateBlogRequestDTO createBlogRequestDTO)
            throws UserNotFoundException, IOException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        Blog blog = new Blog();
        blog.setTitle(createBlogRequestDTO.getTitle());
        blog.setContent(createBlogRequestDTO.getContent());
        blog.setUser(user);

        List<String> imageUrls = new ArrayList<>();
        if (createBlogRequestDTO.getImageFiles() != null) {
            for (MultipartFile file : createBlogRequestDTO.getImageFiles()) {
                String imageUrl = cloudinary.uploader()
                        .upload(file.getBytes(),
                                Map.of("public_id", UUID.randomUUID().toString()))
                        .get("url")
                        .toString();
                imageUrls.add(imageUrl);
            }
        }
        blog.setImageUrl(imageUrls);

        Blog savedBlog = blogRepository.save(blog);

        BlogResponseDTO blogResponseDTO = new BlogResponseDTO();
        blogResponseDTO.setId(savedBlog.getId());
        blogResponseDTO.setTitle(savedBlog.getTitle());
        blogResponseDTO.setContent(savedBlog.getContent());
        blogResponseDTO.setImageUrls(savedBlog.getImageUrl());
        blogResponseDTO.setUserId(userId);
        return blogResponseDTO;


    }

    @Override
    public BlogResponseDTO findBlogById(Long id) throws BlogNotFoundException {

        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new BlogNotFoundException("Blog with id " + id + " not found"));
        BlogResponseDTO blogResponseDTO = new BlogResponseDTO();
        blogResponseDTO.setId(blog.getId());
        blogResponseDTO.setTitle(blog.getTitle());
        blogResponseDTO.setContent(blog.getContent());
        blogResponseDTO.setImageUrls(blog.getImageUrl());
        blogResponseDTO.setUserId(blog.getUser().getId());
        return blogResponseDTO;
    }
}
