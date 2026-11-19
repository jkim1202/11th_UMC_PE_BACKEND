package org.example.umcbackend.favorite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FavoriteStatusResponse(
        Long movieId,
        @JsonProperty("isFavorite") boolean isFavorite
) {}
