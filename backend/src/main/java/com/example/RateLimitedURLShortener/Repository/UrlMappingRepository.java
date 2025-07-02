package com.example.RateLimitedURLShortener.Repository;

import com.example.RateLimitedURLShortener.Entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping,Long> {
    Optional<UrlMapping> findByCode(String code);
}
