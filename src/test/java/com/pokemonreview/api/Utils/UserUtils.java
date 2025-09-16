package com.pokemonreview.api.Utils;

import com.pokemonreview.api.models.UserEntity;

public class UserUtils {

    public static UserEntity createUserA() {
        return UserEntity.builder()
                .username("Mark")
                .password("Mark001")
                .build();
    }

    public static UserEntity createUserB() {
        return UserEntity.builder()
                .username("John")
                .password("John001")
                .build();
    }

    public static UserEntity createUserC() {
        return UserEntity.builder()
                .username("Kane")
                .password("Kane001")
                .build();
    }
    public static UserEntity createUserD() {
        return UserEntity.builder()
                .username("Bob")
                .password("Bob001")
                .build();
    }
}
