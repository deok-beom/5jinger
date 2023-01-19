package com.sparta.ojinger.service;

import com.sparta.ojinger.dto.ElevationRequestResponseDto;
import com.sparta.ojinger.entity.ElevationRequest;
import com.sparta.ojinger.entity.ElevationStatus;
import com.sparta.ojinger.repository.ElevationRequestRepository;
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
public class ElevationRequestService {
    private final ElevationRequestRepository elevationRequestRepository;

    @Transactional(readOnly = true)
    public List<ElevationRequestResponseDto> getAllElevationRequests(Pageable pageable) {
        List<ElevationRequestResponseDto> responseDtoList = new ArrayList<>();
        Page<ElevationRequest> elevations = elevationRequestRepository.findAll(pageable);

        for (ElevationRequest elevationRequest : elevations) {
            ElevationRequestResponseDto responseDto = new ElevationRequestResponseDto(elevationRequest.getId(),
                    elevationRequest.getUser().getId(), elevationRequest.getRequestDate(), elevationRequest.getStatus());
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    @Transactional
    public void updateElevationRequestStatus(Long userId, ElevationStatus status) {
        Optional<ElevationRequest> optionalElevation = elevationRequestRepository.findByUserId(userId);
        if (optionalElevation.isEmpty()) {
            throw new EntityNotFoundException();
        }

        ElevationRequest elevationRequest = optionalElevation.get();
        elevationRequest.setStatus(status);
        elevationRequestRepository.save(elevationRequest);
    }
}
