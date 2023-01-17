package com.sparta.ojinger.repository;

import com.sparta.ojinger.entity.Elevation;
import com.sparta.ojinger.entity.ElevationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ElevationRepository extends JpaRepository<Elevation, Long> {
    List<Elevation> findAllByStatus(ElevationStatus status);

    Optional<Elevation> findByUser(Long id);
}
