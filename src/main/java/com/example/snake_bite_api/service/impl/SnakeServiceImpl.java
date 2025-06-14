package com.example.snake_bite_api.service.impl;

import com.cloudinary.Cloudinary;
import com.example.snake_bite_api.controller.dto.request.CreateSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.SnakeResponseDTO;
import com.example.snake_bite_api.exception.AdminNotFoundException;
import com.example.snake_bite_api.exception.SnakeNotFoundException;
import com.example.snake_bite_api.models.Admin;
import com.example.snake_bite_api.models.Snake;
import com.example.snake_bite_api.models.Venomous;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.repository.SnakeRepository;
import com.example.snake_bite_api.service.SnakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SnakeServiceImpl implements SnakeService {

    @Autowired
    private SnakeRepository snakeRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public SnakeResponseDTO createSnake(Long adminId,
                                        CreateSnakeRequestDTO createSnakeRequestDTO)
            throws IOException, AdminNotFoundException {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("Admin with ID " + adminId + " not found."));

        Snake snake = new Snake();
        snake.setName(createSnakeRequestDTO.getName());
        snake.setColor(createSnakeRequestDTO.getColor());
        snake.setSpecies(createSnakeRequestDTO.getSpecies());
        snake.setPattern(createSnakeRequestDTO.getPattern());
        snake.setAverageLength(createSnakeRequestDTO.getAverageLength());
        snake.setVenomous(Venomous.NON_VENOMOUS);
        snake.setAdmin(admin);

        List<String> imageUrls = new ArrayList<>();
        if (createSnakeRequestDTO.getImageFiles() != null) {
            for (MultipartFile file : createSnakeRequestDTO.getImageFiles()) {
                String imageUrl = cloudinary.uploader()
                        .upload(file.getBytes(),
                                Map.of("public_id", UUID.randomUUID().toString()))
                        .get("url")
                        .toString();
                imageUrls.add(imageUrl);
            }
        }
        snake.setImageUrl(imageUrls);

        Snake savedSnake = snakeRepository.save(snake);
        SnakeResponseDTO snakeResponseDTO = new SnakeResponseDTO();
        snakeResponseDTO.setId(savedSnake.getId());
        snakeResponseDTO.setName(savedSnake.getName());
        snakeResponseDTO.setColor(savedSnake.getColor());
        snakeResponseDTO.setSpecies(savedSnake.getSpecies());
        snakeResponseDTO.setPattern(savedSnake.getPattern());
        snakeResponseDTO.setAverageLength(savedSnake.getAverageLength());
        snakeResponseDTO.setVenomous(savedSnake.getVenomous());
        snakeResponseDTO.setImageUrls(savedSnake.getImageUrl());
        snakeResponseDTO.setAdminId(savedSnake.getAdmin().getId());
        return snakeResponseDTO;

    }

    @Override
    public SnakeResponseDTO findSnakeById(Long id) throws SnakeNotFoundException {

        Snake snake = snakeRepository.findById(id)
                .orElseThrow(() -> new SnakeNotFoundException("Snake with ID " + id + " not found."));
        SnakeResponseDTO snakeResponseDTO = new SnakeResponseDTO();
        snakeResponseDTO.setId(snake.getId());
        snakeResponseDTO.setName(snake.getName());
        snakeResponseDTO.setColor(snake.getColor());
        snakeResponseDTO.setSpecies(snake.getSpecies());
        snakeResponseDTO.setPattern(snake.getPattern());
        snakeResponseDTO.setAverageLength(snake.getAverageLength());
        snakeResponseDTO.setVenomous(snake.getVenomous());
        snakeResponseDTO.setImageUrls(snake.getImageUrl());
        snakeResponseDTO.setAdminId(snake.getAdmin().getId());
        return snakeResponseDTO;
    }

    @Override
    public List<SnakeResponseDTO> findAllSnake() {
        List<Snake> snakes = snakeRepository.findAll();
        return snakes.stream().map(snake -> {
            SnakeResponseDTO snakeResponseDTO = new SnakeResponseDTO();
            snakeResponseDTO.setId(snake.getId());
            snakeResponseDTO.setName(snake.getName());
            snakeResponseDTO.setColor(snake.getColor());
            snakeResponseDTO.setSpecies(snake.getSpecies());
            snakeResponseDTO.setPattern(snake.getPattern());
            snakeResponseDTO.setAverageLength(snake.getAverageLength());
            snakeResponseDTO.setVenomous(snake.getVenomous());
            snakeResponseDTO.setImageUrls(snake.getImageUrl());
            snakeResponseDTO.setAdminId(snake.getAdmin().getId());
            return snakeResponseDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteSnakeById(Long id) throws SnakeNotFoundException {
        if (!snakeRepository.existsById(id)) {
            throw new SnakeNotFoundException("Snake with ID " + id + " not found.");
        }
        snakeRepository.deleteById(id);
    }

    @Override
    public SnakeResponseDTO updateSnakeById(Long id, CreateSnakeRequestDTO createSnakeRequestDTO)
            throws SnakeNotFoundException, IOException {

        Snake existingSnake = snakeRepository.findById(id)
                .orElseThrow(() -> new SnakeNotFoundException("Snake with ID " + id + " not found."));

        existingSnake.setName(createSnakeRequestDTO.getName());
        existingSnake.setColor(createSnakeRequestDTO.getColor());
        existingSnake.setSpecies(createSnakeRequestDTO.getSpecies());
        existingSnake.setPattern(createSnakeRequestDTO.getPattern());
        existingSnake.setAverageLength(createSnakeRequestDTO.getAverageLength());
        existingSnake.setVenomous(createSnakeRequestDTO.getVenomous());

        if (createSnakeRequestDTO.getImageFiles() != null && !createSnakeRequestDTO.getImageFiles().isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : createSnakeRequestDTO.getImageFiles()) {
                String imageUrl = cloudinary.uploader()
                        .upload(file.getBytes(),
                                Map.of("public_id", UUID.randomUUID().toString()))
                        .get("url")
                        .toString();
                imageUrls.add(imageUrl);
            }
            existingSnake.setImageUrl(imageUrls);
        }

        Snake updatedSnake = snakeRepository.save(existingSnake);

        SnakeResponseDTO snakeResponseDTO = new SnakeResponseDTO();
        snakeResponseDTO.setId(updatedSnake.getId());
        snakeResponseDTO.setName(updatedSnake.getName());
        snakeResponseDTO.setColor(updatedSnake.getColor());
        snakeResponseDTO.setSpecies(updatedSnake.getSpecies());
        snakeResponseDTO.setPattern(updatedSnake.getPattern());
        snakeResponseDTO.setAverageLength(updatedSnake.getAverageLength());
        snakeResponseDTO.setVenomous(updatedSnake.getVenomous());
        snakeResponseDTO.setImageUrls(updatedSnake.getImageUrl());
        snakeResponseDTO.setAdminId(updatedSnake.getAdmin().getId());

        return snakeResponseDTO;
    }

}
