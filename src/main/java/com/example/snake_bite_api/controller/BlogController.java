package com.example.snake_bite_api.controller;

import com.example.snake_bite_api.controller.dto.request.CreateBlogRequestDTO;
import com.example.snake_bite_api.controller.dto.response.BlogResponseDTO;
import com.example.snake_bite_api.exception.BlogNotFoundException;
import com.example.snake_bite_api.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping
    public BlogResponseDTO createBlog(@RequestParam Long userId,
                                      @ModelAttribute CreateBlogRequestDTO createBlogRequestDTO)
            throws IOException {
        return blogService.createBlog(userId,createBlogRequestDTO);
    }

    @GetMapping("/{blog-id}")
    public BlogResponseDTO findBlogById(@PathVariable("blog-id") Long blogId) throws BlogNotFoundException {
        return blogService.findBlogById(blogId);
    }
}
