package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByPokemonId(int pokemonId);
}
