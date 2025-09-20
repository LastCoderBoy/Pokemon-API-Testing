package com.pokemonreview.api.Utils;

import com.pokemonreview.api.models.Review;

public class ReviewUtils {

    public static Review createReviewA() {
        return Review.builder()
                .id(1)
                .title("Strong Pokemon")
                .stars(5)
                .content("Really good in battles")
                .pokemon(PokemonUtils.createPokemonA())
                .build();
    }

    public static Review createReviewB() {
        return Review.builder()
                .id(2)
                .title("Average Pokemon")
                .stars(3)
                .content("Not too strong, not too weak")
                .pokemon(PokemonUtils.createPokemonB())
                .build();
    }

    public static Review createReviewC() {
        return Review.builder()
                .id(3)
                .title("Weak Pokemon")
                .stars(2)
                .content("Weak Pokemon, needs training")
                .pokemon(PokemonUtils.createPokemonB())
                .build();
    }
}
