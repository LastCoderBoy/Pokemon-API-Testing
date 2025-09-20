package com.pokemonreview.api.Utils;

import com.pokemonreview.api.models.Pokemon;

public class PokemonUtils {

    public static Pokemon createPokemonA() {
        return Pokemon.builder()
                .id(1)
                .name("Pikachu")
                .type("Electric")
                .build();
    }
    public static Pokemon createPokemonB() {
        return Pokemon.builder()
                .id(2)
                .name("Bulbasaur")
                .type("Grass")
                .build();
    }
    public static Pokemon createPokemonC() {
        return Pokemon.builder()
                .id(3)
                .name("Charmander")
                .type("Fire")
                .build();
    }
    public static Pokemon createPokemonD() {
        return Pokemon.builder()
                .id(4)
                .name("Squirtle")
                .type("Water")
                .build();
    }
}
