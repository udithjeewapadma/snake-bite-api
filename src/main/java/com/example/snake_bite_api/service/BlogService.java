package com.example.snake_bite_api.service;

import com.example.snake_bite_api.controller.dto.request.CreateBlogRequestDTO;
import com.example.snake_bite_api.controller.dto.response.BlogResponseDTO;
import com.example.snake_bite_api.exception.BlogNotFoundException;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.Blog;

import java.io.IOException;
import java.util.List;

public interface BlogService {

    BlogResponseDTO createBlog(Long userId, CreateBlogRequestDTO createBlogRequestDTO)
            throws UserNotFoundException, IOException;

    BlogResponseDTO findBlogById(Long id) throws BlogNotFoundException;

    List<BlogResponseDTO> findAllBlogs();

    void deleteBlogById(Long id) throws BlogNotFoundException;

    BlogResponseDTO updateBlogById(Long id, CreateBlogRequestDTO createBlogRequestDTO)
            throws BlogNotFoundException, IOException;
}
