package org.example.umcbackend.favorite.repository;

import java.util.List;
import java.util.Optional;
import org.example.umcbackend.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByMember_MemberIdOrderByFavoriteIdDesc(Long memberId);
    Optional<Favorite> findByMember_MemberIdAndMovieId(Long memberId, Long movieId);
    boolean existsByMember_MemberIdAndMovieId(Long memberId, Long movieId);
}
