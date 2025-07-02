package com.example.RateLimitedURLShortener.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_mapping" ,indexes = {
        @Index(name = "idx_code", columnList = "Code", unique = true)
})
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "code", nullable = false,unique = true)
    private String code;

    @Column(name = "original_url",nullable = false,length = 2048)
    private String originalUrl;

    @Column(name = "createdAt",nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public UrlMapping() {
    }

    public UrlMapping(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
