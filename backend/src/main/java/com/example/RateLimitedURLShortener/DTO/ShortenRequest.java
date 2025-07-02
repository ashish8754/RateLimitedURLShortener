package com.example.RateLimitedURLShortener.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ShortenRequest {

    @NotBlank(message = "Url should not be blank")
    @Pattern(
            regexp = "^(https?://).+$",
            message = "URL must start with http:// or https://"
    )
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
