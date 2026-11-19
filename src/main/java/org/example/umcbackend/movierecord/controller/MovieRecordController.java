package org.example.umcbackend.movierecord.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.umcbackend.global.interceptor.AuthInterceptor;
import org.example.umcbackend.global.response.ApiResponse;
import org.example.umcbackend.movierecord.dto.CreateMovieRecordRequest;
import org.example.umcbackend.movierecord.dto.MovieRecordResponse;
import org.example.umcbackend.movierecord.service.MovieRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MovieRecordController {

    private final MovieRecordService movieRecordService;

    @PostMapping("/movie-records")
    public ResponseEntity<ApiResponse<MovieRecordResponse>> create(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @Valid @RequestBody CreateMovieRecordRequest request
    ) {
        MovieRecordResponse response = movieRecordService.create(memberId, request);
        return ResponseEntity.status(201).body(ApiResponse.created("영화 기록을 저장했습니다.", response));
    }
}
