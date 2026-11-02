package org.example.umcbackend.rating.repository;

import java.util.List;
import org.example.umcbackend.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByMember_MemberIdOrderByRatingIdDesc(Long memberId);
    boolean existsByMember_MemberIdAndMovieId(Long memberId, Long movieId);
}
