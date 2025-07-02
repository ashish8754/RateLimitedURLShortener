package com.example.RateLimitedURLShortener.Service;

import com.example.RateLimitedURLShortener.Entity.UrlMapping;
import com.example.RateLimitedURLShortener.Repository.UrlMappingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class URLServiceTest {

    @Mock
    private UrlMappingRepository urlMappingRepository;

    @InjectMocks
    private URLService service;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShortUrl_generateAndsave_Test(){
        // allow any code (no collision)
        when(urlMappingRepository.findByCode(anyString())).thenReturn(Optional.empty());
        // capture saved entity
        when(urlMappingRepository.save(org.mockito.ArgumentMatchers.any(UrlMapping.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UrlMapping mapping = service.shortenUrl("https://x.com");
        assertThat(mapping.getOriginalUrl()).isEqualTo("https://x.com");
        assertThat(mapping.getCode()).hasSize(6);
    }

    @Test
    void getByCode_existingCode_returnsMapping() {
        UrlMapping mapping = new UrlMapping();
        mapping.setCode("abc123");
        mapping.setOriginalUrl("https://foo.bar");
        when(urlMappingRepository.findByCode("abc123"))
                .thenReturn(Optional.of(mapping));

        UrlMapping result = service.getByCode("abc123");
        assertThat(result.getOriginalUrl()).isEqualTo("https://foo.bar");
        assertThat(result.getCode()).isEqualTo("abc123");
    }

    @Test
    void getByCode_notFound_throws() {
        when(urlMappingRepository.findByCode("nope")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getByCode("nope"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("code: nope");
    }

}
