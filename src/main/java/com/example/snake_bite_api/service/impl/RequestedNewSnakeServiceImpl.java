package com.example.snake_bite_api.service.impl;

import com.cloudinary.Cloudinary;
import com.example.snake_bite_api.controller.dto.request.CreateNewSnakeRequestDTO;
import com.example.snake_bite_api.controller.dto.response.NewSnakeResponseDTO;
import com.example.snake_bite_api.exception.UserNotFoundException;
import com.example.snake_bite_api.models.RequestedNewSnake;
import com.example.snake_bite_api.models.SnakeRequestStatus;
import com.example.snake_bite_api.models.User;
import com.example.snake_bite_api.models.Venomous;
import com.example.snake_bite_api.repository.AdminRepository;
import com.example.snake_bite_api.repository.RequestedNewSnakeRepository;
import com.example.snake_bite_api.repository.UserRepository;
import com.example.snake_bite_api.service.RequestedNewSnakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RequestedNewSnakeServiceImpl implements RequestedNewSnakeService {

    @Autowired
    private RequestedNewSnakeRepository requestedNewSnakeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public NewSnakeResponseDTO createRequest(Long userId,CreateNewSnakeRequestDTO createNewSnakeRequestDTO) throws  UserNotFoundException, IOException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        RequestedNewSnake requestedNewSnake = new RequestedNewSnake();
        requestedNewSnake.setName(createNewSnakeRequestDTO.getName());
        requestedNewSnake.setColor(createNewSnakeRequestDTO.getColor());
        requestedNewSnake.setSpecies(createNewSnakeRequestDTO.getSpecies());
        requestedNewSnake.setPattern(createNewSnakeRequestDTO.getPattern());
        requestedNewSnake.setAverageLength(createNewSnakeRequestDTO.getAverageLength());
        requestedNewSnake.setVenomous(Venomous.NON_VENOMOUS);
        requestedNewSnake.setStatus(SnakeRequestStatus.PENDING);
        requestedNewSnake.setUser(user);

        List<String> imageUrls = new ArrayList<>();
        if (createNewSnakeRequestDTO.getImageFiles() != null) {
            for (MultipartFile file : createNewSnakeRequestDTO.getImageFiles()) {
                String imageUrl = cloudinary.uploader()
                        .upload(file.getBytes(),
                                Map.of("public_id", UUID.randomUUID().toString()))
                        .get("url")
                        .toString();
                imageUrls.add(imageUrl);
            }
        }
        requestedNewSnake.setImageUrl(imageUrls);

        RequestedNewSnake savedSnake = requestedNewSnakeRepository.save(requestedNewSnake);
        NewSnakeResponseDTO newSnakeResponseDTO = new NewSnakeResponseDTO();
        newSnakeResponseDTO.setId(savedSnake.getId());
        newSnakeResponseDTO.setName(savedSnake.getName());
        newSnakeResponseDTO.setColor(savedSnake.getColor());
        newSnakeResponseDTO.setSpecies(savedSnake.getSpecies());
        newSnakeResponseDTO.setPattern(savedSnake.getPattern());
        newSnakeResponseDTO.setAverageLength(savedSnake.getAverageLength());
        newSnakeResponseDTO.setVenomous(savedSnake.getVenomous());
        newSnakeResponseDTO.setImageUrls(savedSnake.getImageUrl());
        newSnakeResponseDTO.setStatus(savedSnake.getStatus());
        newSnakeResponseDTO.setUserId(savedSnake.getUser().getId());
        return newSnakeResponseDTO;

    }
}
