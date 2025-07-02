package com.example.RateLimitedURLShortener.Controller;

import com.example.RateLimitedURLShortener.DTO.ShortenRequest;
import com.example.RateLimitedURLShortener.DTO.ShortenResponse;
import com.example.RateLimitedURLShortener.Entity.UrlMapping;
import com.example.RateLimitedURLShortener.Service.URLService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.persistence.EntityNotFoundException;
import java.net.URI;

@RestController
@RequestMapping(value = "/api")
public class UrlController {

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);
    private final URLService urlService;
    private final String domain ;

    public UrlController(URLService urlService, @Value("${app.domain:http://localhost:8080}") String domain){
        this.urlService=urlService;
        this.domain=domain;
    }

    @PostMapping(value = "/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(@Valid @RequestBody ShortenRequest request){

        UrlMapping mapping=urlService.shortenUrl(request.getUrl());
        String shortUrl = String.format("%s/api/%s",domain , mapping.getCode());
        ShortenResponse response = new ShortenResponse(mapping.getCode(),shortUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Redirect to original URL",
            responses = {
                    @ApiResponse(responseCode = "302",
                            description = "Redirect to the original URL",
                            headers = @Header(name = "Location",
                                    description = "The target URL"))
            }
    )
    @GetMapping(value = "/{code}")
    public ResponseEntity<Void> redirectUrl(@PathVariable String code) {
        try {
            UrlMapping mapping = urlService.getByCode(code);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(mapping.getOriginalUrl()));
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        } catch (EntityNotFoundException ex) {
            System.out.println("Not found code");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
