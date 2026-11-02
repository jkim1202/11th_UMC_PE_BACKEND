package org.example.umcbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.umcbackend.service.TmdbService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TmdbController {

    private final TmdbService tmdbService;

    @GetMapping("/movie/popular")
    public ResponseEntity<String> popular(HttpServletRequest request) {
        return tmdbService.get("/movie/popular", request.getQueryString());
    }

    @GetMapping("/search/movie")
    public ResponseEntity<String> search(HttpServletRequest request) {
        return tmdbService.get("/search/movie", request.getQueryString());
    }

    @GetMapping("/movie/{id:\\d+}")
    public ResponseEntity<String> detail(@PathVariable long id, HttpServletRequest request) {
        return tmdbService.get("/movie/" + id, request.getQueryString());
    }
}
