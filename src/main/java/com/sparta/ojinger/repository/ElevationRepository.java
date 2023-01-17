package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.Elevation;
import com.sparta.ojinger.entity.ElevationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ElevationRepository extends JpaRepository<Elevation, Long> {
    Page<Elevation> findAllByStatus(ElevationStatus status, Pageable pageable);

    Optional<Elevation> findByUserId(Long id);
}
