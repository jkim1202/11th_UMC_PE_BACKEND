package org.example.umcbackend.movierecord.dto;

import org.example.umcbackend.favorite.dto.FavoriteResponse;
import org.example.umcbackend.rating.dto.RatingResponse;

// 즐겨찾기를 함께 추가하지 않았다면 favorite은 null입니다.
public record MovieRecordResponse(
        RatingResponse rating,
        FavoriteResponse favorite
) {}
