package com.example.RateLimitedURLShortener.Service;

import com.example.RateLimitedURLShortener.Entity.UrlMapping;
import com.example.RateLimitedURLShortener.Repository.UrlMappingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class URLService {


    private final UrlMappingRepository urlMappingRepository;

    private final SecureRandom secureRandom=new SecureRandom();
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public URLService(UrlMappingRepository repository){
        this.urlMappingRepository=repository;
    }

    public UrlMapping shortenUrl(String originalUrl){
        String code;
        do{
            code=generateRandomCode(6);
        }while(urlMappingRepository.findByCode(code).isPresent());

        UrlMapping urlMapping=new UrlMapping(originalUrl);
        urlMapping.setCode(code);
        return urlMappingRepository.save(urlMapping);
    }

    public UrlMapping getByCode(String code){
        return urlMappingRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("No url mapping found for given code: "+code));
    }

    public String generateRandomCode(int length){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<length;i++){
            sb.append(ALPHABET.charAt(secureRandom.nextInt(ALPHABET.length())));
        }

        return sb.toString();
    }




}
