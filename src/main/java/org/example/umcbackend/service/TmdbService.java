package org.example.umcbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class TmdbService {

    private final RestClient tmdbRestClient;
    private final String baseUrl;

    public TmdbService(RestClient tmdbRestClient, @Value("${tmdb.base-url}") String baseUrl) {
        this.tmdbRestClient = tmdbRestClient;
        this.baseUrl = baseUrl;
    }

    // TMDB 응답(상태코드 포함)을 가공 없이 그대로 전달
    public ResponseEntity<String> get(String path, String rawQuery) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .path(path)
                .query(rawQuery)
                .build(true)
                .toUri();

        try {
            ResponseEntity<String> response = tmdbRestClient.get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> { })
                    .toEntity(String.class);

            return ResponseEntity.status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getBody());
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"status_message\":\"TMDB 서버에 연결할 수 없습니다.\"}");
        }
    }
}
