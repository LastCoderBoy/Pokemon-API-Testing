package com.pokemonreview.api.Utils;

import com.pokemonreview.api.models.Review;

public class ReviewUtils {

    public static Review createReviewA() {
        return Review.builder()
                .title("Strong Pokemon")
                .stars(5)
                .content("Really good in battles")
                .build();
    }

    public static Review createReviewB() {
        return Review.builder()
                .title("Average Pokemon")
                .stars(3)
                .content("Not too strong, not too weak")
                .build();
    }

    public static Review createReviewC() {
        return Review.builder()
                .title("Weak Pokemon")
                .stars(2)
                .content("Weak Pokemon, needs training")
                .build();
    }
}
