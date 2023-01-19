package com.sparta.ojinger.service;

import com.sparta.ojinger.dto.ElevationResponseDto;
import com.sparta.ojinger.entity.Elevation;
import com.sparta.ojinger.entity.ElevationStatus;
import com.sparta.ojinger.repository.ElevationRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ElevationService {
    private final ElevationRepository elevationRepository;

    @Transactional(readOnly = true)
    public List<ElevationResponseDto> getAllElevationRequests(Pageable pageable) {
        List<ElevationResponseDto> responseDtoList = new ArrayList<>();
        Page<Elevation> elevations = elevationRepository.findAll(pageable);

        for (Elevation elevation : elevations) {
            ElevationResponseDto responseDto = new ElevationResponseDto(elevation.getId(),
                    elevation.getUser().getId(), elevation.getRequestDate(), elevation.getStatus());
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Transactional
    public void updateElevationStatus(Long userId, ElevationStatus status) {
        Optional<Elevation> optionalElevation = elevationRepository.findByUserId(userId);
        if (optionalElevation.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Elevation elevation = optionalElevation.get();
        elevation.setStatus(status);
        elevationRepository.save(elevation);
    }
}
