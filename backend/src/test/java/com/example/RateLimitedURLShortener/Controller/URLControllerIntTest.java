package com.example.RateLimitedURLShortener.Controller;

import com.example.RateLimitedURLShortener.Repository.UrlMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class URLControllerIntTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UrlMappingRepository repo;

    @BeforeEach
    void cleanup() {
        repo.deleteAll();
    }

    @Test
    void shorten_valid_returns201() throws Exception {
        mvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"https://example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isString())
                .andExpect(jsonPath("$.shortUrl").value(org.hamcrest.Matchers.containsString("/api/")));
    }

    @Test
    void shorten_invalid_returns400() throws Exception {
        mvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"bad\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void redirect_found_returns302() throws Exception {
        // first shorten
        String response = mvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"https://foo.com\"}"))
                .andReturn().getResponse().getContentAsString();
        String code = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(response).get("code").asText();

        // then redirect
        mvc.perform(get("/api/" + code))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://foo.com"));
    }

    @Test
    void redirect_notFound_returns404() throws Exception {
        mvc.perform(get("/api/doesnotexist"))
                .andExpect(status().isNotFound());
    }
}
