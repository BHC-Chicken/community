package com.peachdevops.community.repository;

import com.peachdevops.community.dto.FileDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilesRepository extends JpaRepository<FileDto, Long> {
    Optional<FileDto> findById (Long Id);
}
