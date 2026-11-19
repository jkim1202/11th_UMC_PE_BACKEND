package org.example.umcbackend.favorite.dto;

import java.time.LocalDateTime;
import org.example.umcbackend.favorite.entity.Favorite;

public record FavoriteResponse(
        Long favoriteId,
        Long movieId,
        LocalDateTime createdAt
) {
    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getFavoriteId(), favorite.getMovieId(), favorite.getCreatedAt());
    }
}
